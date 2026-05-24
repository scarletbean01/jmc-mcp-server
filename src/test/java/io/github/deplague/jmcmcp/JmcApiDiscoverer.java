import org.openjdk.jmc.flightrecorder.stacktrace.StacktraceModel;
import java.lang.reflect.Method;

public class JmcApiDiscoverer {
    public static void main(String[] args) {
        System.out.println("Methods of StacktraceModel.Branch:");
        for (Method m : StacktraceModel.Branch.class.getDeclaredMethods()) {
            System.out.println("  " + m.getName() + " -> " + m.getReturnType().getName());
        }
        System.out.println("\nMethods of StacktraceModel.Fork:");
        for (Method m : StacktraceModel.Fork.class.getDeclaredMethods()) {
            System.out.println("  " + m.getName() + " -> " + m.getReturnType().getName());
        }
        System.out.println("\nMethods of StacktraceModel:");
        for (Method m : StacktraceModel.class.getDeclaredMethods()) {
            System.out.println("  " + m.getName() + " -> " + m.getReturnType().getName());
        }
    }
}
