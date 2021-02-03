import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import java.nio.*;

public abstract class Texture2D{
  public final int WIDTH;
  public final int HEIGHT;
  private boolean initialized=false;
  protected final ByteBuffer buffer;
  protected int TextureName;

  public Texture2D(int width, int height){
    WIDTH = width;
    HEIGHT = height;
    buffer = null;
  }

  public Texture2D(TextureImage img){
    WIDTH = img.getWidth();
    HEIGHT = img.getHeight();
    buffer = img.getByteBuffer();
  }

  protected boolean isInit(){
    if(initialized){
      return true;
    }else{
      initialized = true;
      return false;
    }
  }

  public abstract void init(GL2GL3 gl);

  public int getTextureName(){
    return TextureName;
  }

}
