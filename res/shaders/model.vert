#version 330 core
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec2 aTexCoord;
layout (location = 2) in vec3 aNorm;
uniform mat4 uProjView;
uniform mat4 uModel;
out vec2 iTexCoord;
out vec3 iNorm;
void main() {
    iTexCoord = aTexCoord;
    iNorm = normalize(mat3(transpose(inverse(uModel))) * aNorm);
    gl_Position = uProjView * uModel * vec4(aPos, 1.0);
}
