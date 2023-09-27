package com.ncc.data.repository.remote.datasourceImpl

import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.ncc.data.remote.model.DataComment
import com.ncc.data.remote.model.DataHandover
import com.ncc.data.remote.model.DataRoutine
import com.ncc.data.remote.model.DataUser
import com.ncc.data.repository.remote.datasource.MainDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.math.log

class MainDataSourceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val stroage: FirebaseStorage,
    private val auth: FirebaseAuth
) : MainDataSource {

    override fun getRoutine(date: String): Task<QuerySnapshot> {
        val routineCollectionRef =
            firestore.collection("routine").document(date)
                .collection("routine")
        return routineCollectionRef.orderBy("team", Query.Direction.ASCENDING).get()
    }

    override fun getHandover(date: String, team: String): Task<QuerySnapshot> {
        val routineCollectionRef =
            firestore.collection("handover").document(date)
                .collection(team)

        return routineCollectionRef.orderBy("time", Query.Direction.ASCENDING).get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                }
            }
    }

    override fun getUserInfo(uid: String): Task<DocumentSnapshot> {
        Log.d("유저정보 호출 시작", uid)
        val userInfoCollectionRef = firestore.collection("userInfo").document(uid)
        Log.d("NCC유저정보", userInfoCollectionRef.get().toString())
        return userInfoCollectionRef.get()
    }

    override fun updateUserInfo(data: DataUser): Task<Void> {
        val userInfoCollectionRef = firestore.collection("userInfo").document(data.uid)
        Log.d("갱신직전", userInfoCollectionRef.get().toString())
        val updateData = mapOf(
            "team" to data.team,
            "position" to data.position,
        )
        return userInfoCollectionRef.update(updateData)
    }


    override fun setRoutine(data: DataRoutine): Task<Void> {
        val routineDocRef = firestore.collection("routine").document(data.date)
        val routineCollectionRef = routineDocRef.collection("routine")

        return firestore.runTransaction { transaction ->
            val routineDocSnapshot = transaction.get(routineDocRef)
            if (!routineDocSnapshot.exists()) {
                // 문서가 없는 경우: 새로운 문서와 하위 컬렉션을 생성하고 데이터 추가
                transaction.set(routineDocRef, data)
                routineCollectionRef.add(data)
            } else {
                // 문서가 있는 경우: 하위 컬렉션에 데이터 추가
                routineCollectionRef.add(data)
            }
            null  // Transaction 결과 반환
        }
    }


    override fun setHandover(data: DataHandover, team: String): Task<Void> {
//        val imagePathsFlow = saveHandoverImage(data, team)
        //이미지 저장
        val imagePaths = mutableListOf<String>()
        return firestore.runTransaction { transaction ->
//            val imagePaths = imagePathsFlow
            val teamCollectionRef = firestore.collection("handover").document(data.date)
                .collection(team)
//            saveHandoverImage(data, team)
            // 자동으로 생성된 문서에 접근하고 문서 ID를 얻음
            val newHandoverDocRef = teamCollectionRef.document()
            val newHandoverDocId = newHandoverDocRef.id

            // 데이터에 문서 ID를 추가하여 Firestore에 저장
            val dataWithId =
                data.copy(id = newHandoverDocId, position = team) // ID 추가
            Log.d("댓글입력시작", "${team}${dataWithId.toString()}")
            transaction.set(newHandoverDocRef, dataWithId, SetOptions.merge())
            saveHandoverImage(dataWithId, team, newHandoverDocId)
            null // Transaction 결과 반환
        }
    }

    //    transaction.update(
//    commentCollectionRef,
//    "comment",
//    commentList
//    )
    override fun setComments(comment: DataComment, team: String): Task<Void> {
        Log.d("저장시작", "${comment.toString()}${team}")
        return firestore.runTransaction { transaction ->
            val handoverRef = firestore.collection("handover").document(comment.date)
                .collection(team).document(comment.handoverid)
            val handoverDoc = transaction.get(handoverRef)
            Log.d("handoverDoc", "${handoverDoc.exists()}${team}")

            if (handoverDoc.exists()) {
//                Log.d("handoverDoc", "${handoverDoc.exists().toString()}")
                val commentList: MutableList<DataComment> =
                    handoverDoc.get("comment") as? MutableList<DataComment> ?: mutableListOf()
                val id = "${comment.time}${comment.content}"
                val dataComment = comment.copy(id = id)
                Log.d("저장전", "${team}${commentList.toString()}")
                if (comment.ImageSrc!!.isNotEmpty()) {
                    Log.d("저장직전", "${team}${commentList.toString()}")
                    saveCommentImage(commentList, dataComment, team, id)
                } else {
                    Log.d("저장직전1", "${team}${commentList.toString()}")
                    commentList.add(dataComment)
                    transaction.update(handoverRef, "comment", commentList)
                }
            }
            null
        }
    }

    private fun saveHandoverImage(
        data: DataHandover,
        team: String,
        newHandoverDocId: String
    ): List<String>? {
        //이미지 저장
        val imagePaths = mutableListOf<String>()
        //전체 작업 수
        val imageCount = data.ImageSrc?.size
        if (imageCount != null) {
            var uploadedImageCount = 0
            for ((index, imageSrc) in data.ImageSrc!!.withIndex()) {
                val imagePath = "handover/${data.date}/$team/${newHandoverDocId}/${index}.jpg"
                val imageRef = stroage.reference.child(imagePath)
                imageRef.putFile(Uri.parse(imageSrc)).addOnSuccessListener { taskSnapshot ->
                    // 이미지 업로드 성공
                    // 이미지 다운로드 URL 가져오기
                    imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        // downloadUri를 사용하여 이미지 다운로드 URL을 리스트에 추가
                        imagePaths.add(downloadUri.toString())
                        // 모든 이미지 업로드 및 URL 저장이 완료되었을 때 Firestore 데이터에 추가 및 저장
                        uploadedImageCount++
                        if (uploadedImageCount == imageCount) {
                            // Firestore 데이터에 이미지 경로 추가
                            firestore.runTransaction { transaction ->
                                val teamCollectionRef =
                                    firestore.collection("handover").document(data.date)
                                        .collection(team)
                                //  문서 ID
                                val newHandoverDocRef = teamCollectionRef.document(newHandoverDocId)
                                // 데이터에 문서 ID를 추가하여 Firestore에 저장
                                transaction.update(
                                    newHandoverDocRef,
                                    "imageSrc",
                                    imagePaths
                                )
                            }
                        }
                    }
                        .addOnFailureListener { exception ->
                            // 이미지 다운로드 URL 가져오기 실패
                            // 실패 처리 로직 추가
                            Log.d("Image다운로드 url 가져오기 실패", exception.toString())
                        }
                }
                    .addOnFailureListener { exception ->
                        // 이미지 업로드 실패
                        // 실패 처리 로직 추가
                        Log.d("이미지 업로드 실패", exception.toString())
                    }
            }
        }
        return imagePaths
    }

    private fun saveCommentImage(
        commentList: MutableList<DataComment>,
        comment: DataComment, team: String,
        id: String
    ): List<String>? {
        //이미지 저장
        val imagePaths = mutableListOf<String>()
        //전체 작업 수
        val imageCount = comment.ImageSrc?.size
        if (imageCount != null) {
            var uploadedImageCount = 0
            for ((index, imageSrc) in comment.ImageSrc!!.withIndex()) {
                val imagePath =
                    "handover/${comment.date}/$team/${comment.handoverid}/comment/${id}/${index}.jpg"
                val imageRef = stroage.reference.child(imagePath)
                Log.d("img업로드 직접", imageSrc)
                imageRef.putFile(Uri.parse(imageSrc)).addOnSuccessListener { taskSnapshot ->
                    // 이미지 업로드 성공
                    // 이미지 다운로드 URL 가져오기
                    imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        // downloadUri를 사용하여 이미지 다운로드 URL을 리스트에 추가
                        imagePaths.add(downloadUri.toString())
                        // 모든 이미지 업로드 및 URL 저장이 완료되었을 때 Firestore 데이터에 추가 및 저장
                        uploadedImageCount++
                        if (uploadedImageCount == imageCount) {
                            Log.d("댓글 이미지 업로드 완료", commentList.toString())
                            // 모든 이미지가 업로드 및 URL 저장 완료됨
                            // Firestore 데이터에 이미지 경로 추가
                            comment.ImageSrc = imagePaths
                            commentList.add(comment)
                            firestore.runTransaction { transaction ->
                                val commentCollectionRef =
                                    firestore.collection("handover").document(comment.date)
                                        .collection(team).document(comment.handoverid)
                                // 데이터에 문서 ID를 추가하여 Firestore에 저장
                                transaction.update(
                                    commentCollectionRef,
                                    "comment",
                                    commentList
                                )
                                Log.d("댓글 저장 완료", commentList.toString())
                            }
                        }
                    }
                        .addOnFailureListener { exception ->
                            // 이미지 다운로드 URL 가져오기 실패
                            // 실패 처리 로직 추가
                            Log.d("Image다운로드 url 가져오기 실패", exception.toString())
                        }
                }
                    .addOnFailureListener { exception ->
                        // 이미지 업로드 실패
                        // 실패 처리 로직 추가
                        Log.d("이미지 업로드 실패", exception.toString())
                    }
            }
        }
        return imagePaths
    }
}