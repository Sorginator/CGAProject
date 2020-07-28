package cga.exercise.components.`object`

import cga.exercise.components.geometry.Renderable
import cga.exercise.components.shader.ShaderProgram
import cga.framework.ModelLoader
import org.joml.Math
import org.joml.Vector3f

open class texturedObject(path: String, posX: Float, posY: Float, posZ: Float, rotX: Float= 0f, rotY: Float= 0f, rotZ: Float= 0f, scaleX: Float= 1f, scaleY: Float= 1f, scaleZ: Float= 1f) {
    val loadedObject: Renderable
    init {
        loadedObject = ModelLoader.loadModel(path,  Math.toRadians(rotX), Math.toRadians(rotY), Math.toRadians(rotZ))?: throw IllegalArgumentException("Could not load the model")
        loadedObject.scaleLocal(Vector3f(scaleX, scaleY, scaleZ))
        loadedObject.translateGlobal(Vector3f(posX, posY, posZ))
    }

    open fun render(shader: ShaderProgram){
        loadedObject.render(shader)
    }
}