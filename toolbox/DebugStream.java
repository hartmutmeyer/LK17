package toolbox;

import java.io.PrintStream;
import java.text.MessageFormat;

/**
 * @author Jeeeyul 2011. 11. 1. 오후 4:36:51
 * @since M1.10
 * https://jeeeyul.wordpress.com/2012/10/18/make-system-out-println-rocks/
 * Just call DebugStream.activate() when your application start.
 */
public class DebugStream extends PrintStream {
   private static final DebugStream INSTANCE = new DebugStream();
 
   public static void activate() {
      System.setOut(INSTANCE);
   }
 
   private DebugStream() {
      super(System.out);
   }
 
   @Override
   public void println(Object x) {
      showLocation();
      super.println(x);
   }
 
   @Override
   public void println(String x) {
      showLocation();
      super.println(x);
   }
 
   private void showLocation() {
      StackTraceElement element = Thread.currentThread().getStackTrace()[3];
      super.print(MessageFormat.format("({0}.java:{1, number,#}) : ", element.getClassName(), element.getLineNumber()));
   }
}