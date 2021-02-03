import com.jogamp.opengl.*;
import com.jogamp.opengl.util.PMVMatrix;
import java.awt.event.*;

public class OffScreen{
  final int WIDTH, HEIGHT;
  private int fbo;
  private Texture2DRGBA texrgba; //color
  private Texture2DDepth texd; //depth
  public OffScreen(int width, int height){
    WIDTH = width;
    HEIGHT = height;
    texrgba = new Texture2DRGBA(WIDTH, HEIGHT);
    texd = new Texture2DDepth(WIDTH, HEIGHT);
  }

  final static private int[] COLOR_ATTACHMENTS={
    GL2.GL_COLOR_ATTACHMENT0,GL2.GL_COLOR_ATTACHMENT1,
    GL2.GL_COLOR_ATTACHMENT2,GL2.GL_COLOR_ATTACHMENT3,
    GL2.GL_COLOR_ATTACHMENT4,GL2.GL_COLOR_ATTACHMENT5,
    GL2.GL_COLOR_ATTACHMENT6,GL2.GL_COLOR_ATTACHMENT7,
    GL2.GL_COLOR_ATTACHMENT8,GL2.GL_COLOR_ATTACHMENT9,
    GL2.GL_COLOR_ATTACHMENT10,GL2.GL_COLOR_ATTACHMENT11,
    GL2.GL_COLOR_ATTACHMENT12,GL2.GL_COLOR_ATTACHMENT13,
    GL2.GL_COLOR_ATTACHMENT14,GL2.GL_COLOR_ATTACHMENT15};

  public void init(GL2GL3 gl){
    int[] id = new int[1];
    texrgba.init(gl);
    texd.init(gl);

    gl.glGenFramebuffers(1,id,0);
    fbo = id[0];
    gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, fbo);
    gl.glFramebufferTexture2D(GL.GL_FRAMEBUFFER,
                              GL2.GL_COLOR_ATTACHMENT0,
                              GL2.GL_TEXTURE_2D,
                              texrgba.getTextureName(),0);
    gl.glFramebufferTexture2D(GL.GL_FRAMEBUFFER,
                              GL.GL_DEPTH_ATTACHMENT,
                              GL2.GL_TEXTURE_2D,
                              texd.getTextureName(), 0);
    gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
  }

  /*
  public void init(GL2 gl){
    int[] id = new int[2];
    gl.glGenTextures(2, id, 0);
    TextureName0 = id[0];
    TextureName1 = id[1];
    gl.glEnable(GL.GL_TEXTURE_2D);
    gl.glActiveTexture(GL.GL_TEXTURE0);
    gl.glBindTexture(GL2.GL_TEXTURE_2D, TextureName0);
    gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER,
                       GL.GL_NEAREST);
    //                 GL.GL_LINEAR);
    gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER,
                       GL.GL_NEAREST);
    //                       GL.GL_LINEAR);
    gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S,
                       GL2.GL_CLAMP);
    gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T,
                       GL2.GL_CLAMP);

    gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, GL.GL_RGBA8, WIDTH,
                    HEIGHT, 0, GL.GL_BGRA, GL.GL_UNSIGNED_BYTE,
                    null);

    gl.glBindTexture(GL2.GL_TEXTURE_2D, TextureName1);
    gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER,
                       GL.GL_NEAREST);
    //                 GL.GL_LINEAR);
    gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL.GL_TEXTURE_MAG_FILTER,
                       GL.GL_NEAREST);
    //                       GL.GL_LINEAR);
    gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_S,
                       GL2.GL_CLAMP);
    gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL.GL_TEXTURE_WRAP_T,
                       GL2.GL_CLAMP);

    gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_DEPTH_COMPONENT, WIDTH,
                    HEIGHT, 0, GL2.GL_DEPTH_COMPONENT, GL.GL_UNSIGNED_BYTE,
                    null);

    gl.glGenFramebuffers(1,id,0);
    fbo = id[0];
    gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, fbo);
    gl.glFramebufferTexture2D(GL.GL_FRAMEBUFFER,
                              GL2.GL_COLOR_ATTACHMENT0,
                              GL2.GL_TEXTURE_2D,
                              TextureName0,0);
    gl.glFramebufferTexture2D(GL.GL_FRAMEBUFFER,
                              GL.GL_DEPTH_ATTACHMENT,
                              GL2.GL_TEXTURE_2D,
                              TextureName1,0);
    gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
  }
  */

  public Texture2DRGBA getColorTexture(){
    return texrgba;
  }

  public Texture2DDepth getDepthTexture(){
    return texd;
  }

  public void bind(GL2GL3 gl){
    gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, fbo);
    gl.glDrawBuffers(1, COLOR_ATTACHMENTS, 0);
  }

  public void unbind(GL2GL3 gl){
    gl.glBindFramebuffer(GL.GL_FRAMEBUFFER, 0);
  }

}
