package cn.xiaochuankeji.gift.filter

class AlphaFilter: BasicFilter() {

    override fun getFragmentShader(): String {
        return """
            precision mediump float;
            uniform sampler2D $UNIFORM_TEXTURE0;
            varying vec2 $VARYING_TEXCOORD;
	        void main(){
		        vec2 uvLeft = vec2($VARYING_TEXCOORD.x/2.0,$VARYING_TEXCOORD.y); 
    	        vec4 color = texture2D($UNIFORM_TEXTURE0, uvLeft);
    	        vec2 uvRight = vec2($VARYING_TEXCOORD.x/2.0+0.5,$VARYING_TEXCOORD.y); 
    	        vec4 grey = texture2D($UNIFORM_TEXTURE0, uvRight);
    	        gl_FragColor = vec4(color.rgb, grey.r);
	        }
        """.trimIndent()
    }
}