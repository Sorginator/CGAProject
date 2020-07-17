package cga.exercise.components.geometry

import cga.exercise.components.texture.Texture2D
import cga.framework.OBJLoader
import org.joml.Vector2f
import org.joml.Vector4i
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30

class loadedObject(
        objectPath : String, diffTexturPath: String, emitTexturPath: String, specTexturPath: String,
        texturMipmap : Boolean = true,
        diffTexParamWrapS : Int = GL30.GL_REPEAT, diffTexParamWrapT : Int = GL30.GL_REPEAT, diffTexParamMinFilter : Int = GL30.GL_LINEAR_MIPMAP_LINEAR, diffTexParamMagFilter : Int = GL30.GL_LINEAR,
        emitTexParamWrapS : Int = GL30.GL_REPEAT, emitTexParamWrapT : Int = GL30.GL_REPEAT, emitTexParamMinFilter : Int = GL30.GL_LINEAR_MIPMAP_LINEAR, emitTexParamMagFilter : Int = GL30.GL_LINEAR,
        specTexParamWrapS : Int = GL30.GL_REPEAT, specTexParamWrapT : Int = GL30.GL_REPEAT, specTexParamMinFilter : Int = GL30.GL_LINEAR_MIPMAP_LINEAR, specTexParamMagFilter : Int = GL30.GL_LINEAR,
        materialShininess : Float = 60f, materialTcMultiplier : Vector2f = Vector2f(64.0f, 64.0f)) {

    var meshData : MutableList<OBJLoader.OBJMesh>
    var createdMeshes : MutableList<Mesh>
    val diffTextur : Texture2D
    val emitTextur : Texture2D
    val specTextur : Texture2D
    val objectMaterial : Material
    var renderableObject : Renderable

    init {
        // Laden der Objektdaten & speichern der Meshdaten
        val loadedData = OBJLoader.loadOBJ(objectPath)
        meshData = loadedData.objects[0].meshes
        // Erstellen der Texturen
        diffTextur = Texture2D(diffTexturPath, texturMipmap)
        diffTextur.setTexParams(diffTexParamWrapS, diffTexParamWrapT, diffTexParamMinFilter, diffTexParamMagFilter)
        emitTextur = Texture2D(emitTexturPath, texturMipmap)
        emitTextur.setTexParams(emitTexParamWrapS, emitTexParamWrapT, emitTexParamMinFilter, emitTexParamMagFilter)
        specTextur = Texture2D(specTexturPath, texturMipmap)
        specTextur.setTexParams(specTexParamWrapS, specTexParamWrapT, specTexParamMinFilter, specTexParamMagFilter)
        // Erstellen des Materials mit den erstellten Texturen
        objectMaterial = Material(diffTextur, emitTextur, specTextur, materialShininess, materialTcMultiplier)
        // Erstellen der Meshes
        createdMeshes = mutableListOf()
        for ((index, aktMeshData) in meshData.withIndex()) {
            createdMeshes.add(
                    Mesh(
                            aktMeshData.vertexData,
                            aktMeshData.indexData,
                            arrayOf(
                                    VertexAttribute(3, GL11.GL_FLOAT, 32, 0),
                                    VertexAttribute(2, GL11.GL_FLOAT, 32, 12),
                                    VertexAttribute(3, GL11.GL_FLOAT, 32, 20)
                            ),
                            objectMaterial
                    )
            )
        }
        // Aus den erstellten Daten ein Renderable Object erstellen
        renderableObject = Renderable(createdMeshes)
    }
}