#version 330 core
layout (location = 0) out vec4 oColorAttachment0;
in vec3 iNorm;
in vec2 iTexCoord;
uniform vec3 uViewPos;
uniform vec3 uLightPos;
uniform float uLevels;
float posterizeAmount(float grayscale) {
    float lower     = floor(grayscale * uLevels) / uLevels;
    float lowerDiff = abs(grayscale - lower);
    float upper     = ceil(grayscale * uLevels) / uLevels;
    float upperDiff = abs(grayscale - upper);
    float level     = lowerDiff <= upperDiff ? lower : upper;
    float adjustment= level / grayscale;
    return adjustment;
}
void main() {
    vec3 lightDir = normalize(uLightPos - iNorm);
    float ambient = 0.2;
    float diffuse = max(dot(iNorm, lightDir), 0.0);
    float grayscale = min(ambient + diffuse, 1.0);
    oColorAttachment0 = vec4(vec3(grayscale * posterizeAmount(grayscale)), 1.0);
}