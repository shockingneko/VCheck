package com.edvard.poseestimation.data

class history {

    var id: Int = 0
    var project:String? = null
    var time: String? = null
    var result_id:Int = 0
    var result:String? = null

    constructor()

    constructor(id:Int,project:String,result_id:Int){
        this.id = id
        this.project = project
        this.time = time
        this.result_id = result_id
        this.result = result
    }

}