package com.ncc.data.widget.extension

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import com.ncc.data.mapper.MainMapper
import com.ncc.data.remote.model.DataRoutine

//fun List<QuerySnapshot>.toRoutine(): List<MutableList<DataRoutine>> {
//    val dataRoutineList = this.map { snapshot ->
//        snapshot.toObjects(DataRoutine::class.java)
//    }
//    return dataRoutineList
//}

fun <Task>QuerySnapshot.toRoutine(): List<DataRoutine> {
    val dataRoutineList = this.map { snapshot ->
        snapshot.toObject(DataRoutine::class.java)
    }

    println("TEST TEST TEST : $dataRoutineList")
    return dataRoutineList
}
//fun Task<QuerySnapshot>.toRoutine(): List<DataRoutine> {
//    val dataRoutineList = this.map { snapshot ->
//        snapshot.toObject(DataRoutine::class.java)
//    }
//
//    println("TEST TEST TEST : $dataRoutineList")
//    return dataRoutineList
//}