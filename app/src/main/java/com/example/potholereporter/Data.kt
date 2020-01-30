package com.example.potholereporter

data class Data(
    var id: String,
    var region: String,
    var desc: String,
    var latitude: String,
    var longitude: String
) {
    constructor() : this("", "", "", "", "") {

    }
}
