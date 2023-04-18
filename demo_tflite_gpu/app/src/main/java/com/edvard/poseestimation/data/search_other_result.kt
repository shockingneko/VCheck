package com.edvard.poseestimation.data

class search_other_result {

    var project:String? = null
    var result_id:Int = 0
    var left_eye:Int = 0
    var right_eye:Int = 0
    var result:String? = null

    constructor()

    constructor(project:String,result_id:Int,left_eye:Int,right_eye:Int,result:String){
        this.project = project
        this.result_id = result_id
        this.left_eye = left_eye
        this.right_eye = right_eye
        this.result = result
    }
}