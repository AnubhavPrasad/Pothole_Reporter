package com.example.potholereporter

import android.media.Image
import android.net.Uri

data class Data (var id:String,var region:String,var desc:String,var latitude:String,var longitude:String){
    constructor():this("","","","",""){

    }
}
