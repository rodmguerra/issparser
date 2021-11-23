package issparser;

import issparser.playernames.Module;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class App {
    private static final List<String> VALID_OPTIONS = Arrays.asList("read", "write");


    public static void main(String[] args) throws IOException {

        String operation = args[0];
        if(args.length == 0 || !VALID_OPTIONS.contains(operation)) {
            System.out.println("First argument must be read/write");
            return;
        }

        if(args.length == 1) {
            System.out.println("Please specify a rom file to " + operation);
            return;
        }

        Container container = new Container(args[1]);


        for (Module module : container.modules()) {
            if(operation.equals("read")) {
                module.readOperation();
            } else if(operation.equals("write")) {
                module.writeOperation();
            }
        }

    }
}
