package cga.exercise.components.light

import cga.exercise.components.geometry.Transformable
import cga.exercise.components.shader.ShaderProgram
import org.joml.Vector3f

open class PointLight(var light_position: Vector3f, var attenuation: Vector3f, var light_color: Vector3f, p: Transformable?) : Transformable(p), IPointLight {

    init
    {
        translateGlobal(light_position)
    }

    override fun bind(shaderProgram: ShaderProgram, name: String) {
        shaderProgram.setUniform(name+"_position", getWorldPosition())
        shaderProgram.setUniform(name+"_color", light_color)
        shaderProgram.setUniform(name+"_attenuation", attenuation)
    }


}