package com.edvard.poseestimation.data

class personal {

    var id:Int = 0
    var name:String? = null
    var image:Int = 0
    var height:Int = 0
    var password: String? = null

    constructor()

    constructor(id:Int, name:String, image: Int, height:Int, password: String){
        this.id = id
        this.name = name
        this.image = image
        this.height = height
        this.password = password
    }

}