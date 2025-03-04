#version 330 core
in vec2 iTexCoord;
uniform sampler2D uTexture;
uniform float uLevels;
float posterizeAmount(vec3 color) {
    float grayscale = max(color.r, max(color.g, color.b));
    float lower     = floor(grayscale * uLevels) / uLevels;
    float lowerDiff = abs(grayscale - lower);
    float upper     = ceil(grayscale * uLevels) / uLevels;
    float upperDiff = abs(grayscale - upper);
    float level     = lowerDiff <= upperDiff ? lower : upper;
    float adjustment= level / grayscale;
    return max(adjustment, 1.0 / uLevels);
}
void main() {
    vec3 color = vec3(texture2D(uTexture, iTexCoord));
    float amount = posterizeAmount(color);
    gl_FragColor = vec4(color * amount, 1.0);
}