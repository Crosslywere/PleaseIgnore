#version 330 core
layout (location = 0) in vec2 aPos;
layout (location = 1) in vec2 aTexCoord;
out vec2 iTexCoord;
void main() {
    iTexCoord = aTexCoord;
    gl_Position = vec4(aPos, 0., 1.);
}