#version 330 core
in vec2 iTexCoord;
void main() {
    gl_FragColor = vec4(iTexCoord, 0., 1.);
}