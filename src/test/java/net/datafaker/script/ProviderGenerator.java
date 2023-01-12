package net.datafaker.script;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.WordUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

class ProviderGenerator {

    @SuppressWarnings("FieldCanBeLocal")
    private final String packageName = "net.datafaker.providers.show";

    public static void main(String[] args) throws FileNotFoundException {
        new ProviderGenerator().generateProvider();
    }

    void generateProvider() throws FileNotFoundException {
        File dir = new File("src/main/resources/en");

        File[] files = dir.listFiles((dir1, name) -> name.toLowerCase().endsWith("supernatural.yml"));

        List<File> fileList = Arrays.asList(files);
        Collections.shuffle(fileList);
        List<File> filesToProcess = fileList.stream().limit(5).collect(Collectors.toList());

        System.out.println(files.length + " files");

        for (File file : filesToProcess) {
            final Map<String, Object> valuesMap = new Yaml().loadAs(new FileReader(file), Map.class);

            Map<String, Object> en = (Map<String, Object>) valuesMap.get("en");
            Map<String, Object> faker = (Map<String, Object>) en.get("faker");

            System.out.println(file);
            processFaker(file, faker);
        }
    }

    private void processFaker(File file, Map<String, Object> faker) {
        String key = (String) faker.keySet().toArray()[0];
        Map<String, Object> subject = (Map<String, Object>) faker.get(key);

        Set<String> strings = subject.keySet();

        createCreator(file, key, strings, packageName);
        createTest(file, strings, packageName);
        createFakerRegistration(file, strings, packageName);
    }

    private void createFakerRegistration(File file, Set<String> strings, String packageName) {
        String className = toJavaConvention(file.getName().substring(0, file.getName().indexOf(".")));
        String methodName = StringUtils.uncapitalize(toJavaConvention(className));

        System.out.println();
        System.out.println("default " + className + " " + methodName + "() {");
        System.out.println("    return getProvider(" + className + ".class, " + className + "::new);");
        System.out.println("}");
    }

    private void createCreator(File file, String key, Set<String> strings, String packageName) {
        String className = toJavaConvention(file.getName().substring(0, file.getName().indexOf(".")));

        System.out.println("package " + packageName + ";");
        System.out.println();
        System.out.println("import net.datafaker.providers.base.AbstractProvider;");
        System.out.println("import net.datafaker.providers.base.BaseProviders;");
        System.out.println();
        System.out.println("/**");
        System.out.println(" * @since 1.8.0");
        System.out.println(" */");
        System.out.println("public class " + className + " extends AbstractProvider<BaseProviders> {");
        System.out.println();
        System.out.println("    protected " + className + "(BaseProviders faker) {");
        System.out.println("        super(faker);");
        System.out.println("    }");
        System.out.println();

        for (String string : strings) {
            String methodName = StringUtils.uncapitalize(toJavaConvention(string));

            System.out.println("    public String " + methodName + "() {\n" +
                "        return resolve(\"" + key + "." + string + "\");\n" +
                "    }");
            System.out.println();
        }

        System.out.println("}");
    }

    private void createTest(File file, Set<String> strings, String packageName) {
        String className = toJavaConvention(file.getName().substring(0, file.getName().indexOf(".")));
        // replace the first letter with a lowercase letter
        String methodName = StringUtils.uncapitalize(toJavaConvention(className));

        System.out.println("package " + packageName + ";");
        System.out.println();
        System.out.println("import org.junit.jupiter.api.Test;");
        System.out.println("import static org.assertj.core.api.AssertionsForClassTypes.assertThat;");

        System.out.println();
        System.out.println("class " + className + "Test extends net.datafaker.AbstractFakerTest {");
        System.out.println();

        for (String string : strings) {
            String testMethodName = StringUtils.uncapitalize(toJavaConvention(string));

            System.out.println("    @Test");
            System.out.println("    void " + testMethodName + "() {");
            System.out.println("        assertThat(faker." + methodName + "()." + testMethodName + "()).isNotEmpty();");
            System.out.println("    }");
            System.out.println();
        }

        System.out.println("}");
    }

    private String toJavaConvention(String baseName) {

        // replace underscores with spaces
        String withoutUnderscore = baseName.replaceAll("_", " ");
        // for every word in the name, capitalize the first letter
        String capitalizedWords = WordUtils.capitalize(withoutUnderscore);
        // remove all spaces
        return capitalizedWords.replaceAll(" ", "");
    }
}
