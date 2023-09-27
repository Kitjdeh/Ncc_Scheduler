package com.ncc.domain.model

data class DomainUser(
    val id: Int,
    val name: String,
    var team: String,
    var position: String,
    val uid: String,
) {
    constructor() : this(0, "", "", "", "")
}
