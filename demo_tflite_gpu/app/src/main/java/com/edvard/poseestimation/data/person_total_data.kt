package com.edvard.poseestimation.data

class person_total_data {

    var id:Int = 0
    var name:String? = null
    var image:Int = 0
    var height:Int = 0
    var password: String? = null
    var forget_question: String? = null
    var forget_answer: String? = null

    constructor()

    constructor(id:Int, name:String, image: Int, height:Int, password: String, forget_question: String, forget_answer: String){
        this.id = id
        this.name = name
        this.image = image
        this.height = height
        this.password = password
        this.forget_question = forget_question
        this.forget_answer = forget_answer
    }
}