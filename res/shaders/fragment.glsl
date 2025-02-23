#version 330 core
in vec2 iTexCoord;

float quantizeAdjustment(vec3 color) {
    float grayscale = max(color.r, max(color.g, color.b));
    return grayscale;
}

void main() {

    gl_FragColor = vec4(iTexCoord, 0., 1.);
}