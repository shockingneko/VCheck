package com.edvard.poseestimation.data

class search_colorblind_result {

    var result_id:Int = 0
    var weak_green:Int = 0
    var weak_red:Int = 0
    var result:String? = null

    constructor()

    constructor(result_id:Int,weak_red:Int,weak_green:Int,result:String){
        this.result_id = result_id
        this.weak_green = weak_green
        this.weak_red = weak_green
        this.result = result
    }
}