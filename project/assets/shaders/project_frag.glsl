#version 330 core

//input from vertex shader
in struct VertexData
{
    vec3 normal;
    vec2 texKoords;
    vec3 toCamera;
    vec3 toPointLight;
    vec3 toSpotLight;
} vertexData;

//uniforms!
//material
uniform sampler2D emit_tex;
uniform sampler2D diff_tex;
uniform sampler2D spec_tex;
uniform float shininess;
//spotlight
uniform float spot_inner_cone;
uniform float spot_outer_cone;
uniform vec3 spot_color;
uniform vec3 spot_direction;
uniform vec3 spot_attenuation;
//pointlight
uniform vec3 point_color;
uniform vec3 point_attenuation;

uniform vec3 colo;

//fragment shader output
out vec4 color;

vec3 phong(vec3 n, vec3 l, vec3 v, vec3 diff_c, vec3 spec_c, float shine)
{
    float cosa = max(dot(n, l), 0.0f);
    float cosb = max(dot(reflect(-l, n), v), 0.0f);
    return diff_c * cosa + spec_c * pow(cosb, shine);
}

float attenuation(float distance, vec3 att_params)
{
    return 1.0f / (att_params.x + att_params.y * distance + att_params.z * distance * distance);
}

float kegel(float inner_cone, float outer_cone, vec3 toLight, vec3 direction)
{
    float theta = dot(-toLight, direction);
    return clamp((theta - outer_cone)/(inner_cone - outer_cone), 0.0f, 1.0f);
}

void main(){
    //Normalisierter Scheißdreck
    vec3 N = normalize(vertexData.normal);
    vec3 V = normalize(vertexData.toCamera);
    float DP = length(vertexData.toPointLight);
    float DS = length(vertexData.toSpotLight);
    vec3 LP = vertexData.toPointLight/DP;
    vec3 LS = vertexData.toSpotLight/DS;
    //Farbwerte der Texturen
    vec3 emit_c = texture(emit_tex, vertexData.texKoords).rgb;
    vec3 diff_c = texture(diff_tex, vertexData.texKoords).rgb;
    vec3 spec_c = texture(spec_tex, vertexData.texKoords).rgb;
    //Ankommende Lichtintensität
    vec3 LIP = point_color * attenuation(DP, point_attenuation);
    vec3 LIS = spot_color * attenuation(DP, spot_attenuation) * kegel(spot_inner_cone, spot_outer_cone, LS, spot_direction);
    //Emmissiver Part
    vec3 res = emit_c * colo;
    //Ambient Part
    res += point_color * 0.1f * diff_c;
    //Lichter
    res += phong(N, LP, V, diff_c, spec_c, shininess) * LIP;
    //res += phong(N, LS, V, diff_c, spec_c, shininess) * LIS;
    color = vec4(res, 1.0f);
}
