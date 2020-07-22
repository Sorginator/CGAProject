package cga.exercise.components.`object`

import cga.exercise.components.geometry.Renderable
import cga.exercise.components.shader.ShaderProgram
import cga.framework.ModelLoader
import org.joml.Math
import org.joml.Vector3f

class GrasGround(var widthX: Int, var widthY: Int, var startX: Float, var startY: Float) {
    //Breite eines Grasobjektes: 3x3
    //Die Breite wird in Anzahl Objekte angegeben
    val grasObjects: MutableList<Renderable>
    val aktGrasObject: Renderable
    lateinit var aktGrasObjectCopy: Renderable

    init {
        grasObjects = mutableListOf()
        //Minimum von Breite in X- und Y-Richtung ist 1
        if (widthX < 1) {
            widthX = 1
        }
        if (widthY < 1) {
            widthY = 1
        }
        aktGrasObject = ModelLoader.loadModel("assets/complex objects/gras/10450_Rectangular_Grass_Patch_v1_iterations-2.obj",  Math.toRadians(-90f), Math.toRadians(90f), 0f)?: throw IllegalArgumentException("Could not load the model")
        //Erstellen des Grasteppichs in der angegebenen Größenordnung
        for (x in 1..widthX) {
            for (y in 1..widthY){
                //Erstellen eines neuen Grasabschnitts
                aktGrasObjectCopy = Renderable(aktGrasObject.meshes)
                aktGrasObjectCopy.translateGlobal(Vector3f(2f + (x*3f - 3f) + startX, 0f - 0.1f, 3f + (y*3f - 3f) + startY))
                aktGrasObjectCopy.scaleLocal(Vector3f(0.01f, 0.01f, 0.01f))
                grasObjects.add(aktGrasObjectCopy)
            }
        }
    }

    fun render(shader: ShaderProgram){
        grasObjects.forEach{
            it.render(shader)
        }
    }
}