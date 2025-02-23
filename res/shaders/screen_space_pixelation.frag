#version 330 core
uniform sampler2D uScreenTexture;
uniform vec2 uAspectRatio;
uniform float uPixels;
in vec2 iTexCoord;

vec2 pixelateTexCoord(vec2 texCoord, vec2 aspectRatio, float pixels) {
    float dx = aspectRatio.y * (1. / pixels);
    float dy = aspectRatio.x * (1. / pixels);
    return vec2(dx * floor(texCoord.x / dx), dy * floor(texCoord.y / dy));
}

void main() {
    vec2 pixelatedTexCoord = pixelateTexCoord(iTexCoord, uAspectRatio, uPixels);
    gl_FragColor = texture2D(uScreenTexture, pixelatedTexCoord);
}