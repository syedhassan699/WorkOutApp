package com.example.workout

class ExerciseModel (
    private var id:Int,
    private var name:String,
    private var img:Int,
    private var isCompleted: Boolean,
    private var isSelected: Boolean,
) {
    fun getId():Int{
        return id;
    }
    fun setId(id:Int){
        this.id = id
    }
    fun getName():String{
        return name;
    }

    fun setName(name: String){
        this.name = name
    }
    fun getImg():Int{
        return img;
    }

    fun setImg(img: Int){
        this.img = img
    }
    fun getIsCompleted():Boolean{
        return isCompleted;
    }
    fun setIsCompleted(isCompleted: Boolean){
        this.isCompleted = isCompleted
    }
    fun getIsSelected():Boolean{
        return isSelected;
    }
    fun setIsSelected(isSelected: Boolean){
        this.isSelected = isSelected
    }
}