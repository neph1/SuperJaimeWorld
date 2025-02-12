#import "Common/ShaderLib/GLSLCompat.glsllib"
#import "Common/ShaderLib/Skinning.glsllib"
#import "Common/ShaderLib/Instancing.glsllib"
#import "Common/ShaderLib/MorphAnim.glsllib"

attribute vec3 inPosition;

#if defined(HAS_COLORMAP) || (defined(HAS_LIGHTMAP) && !defined(SEPARATE_TEXCOORD))
    #define NEED_TEXCOORD1
#endif

attribute vec2 inTexCoord;
attribute vec2 inTexCoord2;
attribute vec4 inColor;
attribute vec4 inNormal;

varying vec2 texCoord1;
varying vec2 texCoord2;

varying vec4 vertColor;
varying float normalY;
#ifdef HAS_POINTSIZE
    uniform float m_PointSize;
#endif

#ifdef RENDER_DISTANCE
    uniform float m_RenderDistance;
#endif

void main(){
    #ifdef NEED_TEXCOORD1
        texCoord1 = inTexCoord;
    #endif

    #ifdef SEPARATE_TEXCOORD
        texCoord2 = inTexCoord2;
    #endif

    #ifdef HAS_VERTEXCOLOR
        vertColor = inColor;
    #endif

    #ifdef HAS_POINTSIZE
        gl_PointSize = m_PointSize;
    #endif

    vec4 modelSpacePos = vec4(inPosition, 1.0);

    #ifdef NUM_MORPH_TARGETS
        Morph_Compute(modelSpacePos);
    #endif

    #ifdef NUM_BONES
        Skinning_Compute(modelSpacePos);
    #endif
    
    normalY = clamp(0.15 - inNormal.y * 0.15, -0.5, 0.0);
    
    vec4 worldView = TransformWorldView(modelSpacePos);
    

    gl_Position = TransformWorldViewProjection(modelSpacePos);
    
    #ifdef RENDER_DISTANCE
        if (gl_Position.w > m_RenderDistance) {
            normalY = -100;
        }
    #endif
}