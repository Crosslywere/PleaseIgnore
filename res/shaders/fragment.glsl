#version 330 core
uniform sampler2D uTexture;
in vec2 iTexCoord;
void main() {
    gl_FragColor = texture2D(uTexture, iTexCoord);
}