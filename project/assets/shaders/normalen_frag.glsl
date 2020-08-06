#version 330 core

//input from vertex shader
in struct VertexData
{
    vec3 normal;
} vertexData;

//uniforms!
//material

uniform vec3 colo;

//fragment shader output
out vec4 color;

void main(){
    //Normalisierter Scheißdreck
    vec3 N = normalize(vertexData.normal);
    color = vec4(N, 1.0f);
}

