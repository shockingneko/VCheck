package com.edvard.poseestimation.data

class search_degree_result {

    var result_id:Int = 0
    var eye:Int = 0
    var degree_result:Double = 0.0
    var result:String? = null

    constructor()

    constructor(result_id:Int,eye:Int,degree_result:Double,result:String){
        this.result_id = result_id
        this.eye = eye
        this.degree_result = degree_result
        this.result = result
    }
}