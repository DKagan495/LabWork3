import com.sun.jdi.Value;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.*;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
    // Константы с исходным размером окна приложения
    private static final int WIDTH = 700;
    private static final int HEIGHT = 500;
    // Массив коэффициентов многочлена
    private Double[] coefficients;
    private JTable table;
    // Объект диалогового окна для выбора файлов
// Компонент не создаѐтся изначально, т.к. может и не понадобиться
// пользователю если тот не собирается сохранять данные в файл
    private JFileChooser fileChooser = null;
    // Элементы меню вынесены в поля данных класса, так как ими необходимо
// манипулировать из разных мест
    private JMenuItem saveToTextMenuItem;
    private JMenuItem saveToGraphicsMenuItem;
    private JMenuItem searchValueMenuItem;
    private JMenuItem aboutProgramItem;
    private JMenuItem easyNumbersItem;
    private JMenuItem saveToCSVItem;
    // Поля ввода для считывания значений переменных
    private JTextField textFieldFrom;
    private JTextField textFieldTo;
    private JTextField textFieldStep;
    private Box hBoxResult;
    // Визуализатор ячеек таблицы
    private GornerTableCellRenderer renderer = new GornerTableCellRenderer();
    // Модель данных с результатами вычислений
    private GornerTableModel data;
    public MainFrame(Double[] coefficients) {
// Обязательный вызов конструктора предка
        super("Табулирование многочлена на отрезке по схеме Горнера");
// Запомнить во внутреннем поле переданные коэффициенты
        this.coefficients = coefficients;
// Установить размеры окна
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();
// Отцентрировать окно приложения на экране
        setLocation((kit.getScreenSize().width - WIDTH)/2,
                (kit.getScreenSize().height - HEIGHT)/2);
// Создать меню
        JMenuBar menuBar = new JMenuBar();
// Установить меню в качестве главного меню приложения
        setJMenuBar(menuBar);
// Добавить в меню пункт меню "Файл"
        JMenu fileMenu = new JMenu("Файл");
// Добавить его в главное меню
        menuBar.add(fileMenu);
// Создать пункт меню "Таблица"
        JMenu tableMenu = new JMenu("Таблица");
// Добавить его в главное меню
        menuBar.add(tableMenu);
        JMenu aboutProgram = new JMenu("Справка");
        menuBar.add(aboutProgram);
        //Create ability to save table to the .csv file
        Action saveToCSVAction = new AbstractAction("Сохранить в CSV") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(fileChooser == null)
                {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION)
// Если результат его показа успешный,
// сохранить данные в текстовый файл
                    saveToCSV(fileChooser.getSelectedFile());

            }
        };
        saveToCSVItem = fileMenu.add(saveToCSVAction);
        saveToCSVItem.setEnabled(false);
        //Create adv. functional
        Action easyNumbersAction = new AbstractAction("Найти близкие к простым") {
            @Override
            public void actionPerformed(ActionEvent e) {
                renderer.setEasyNeedle("1");
                getContentPane().repaint();
                //renderer.setEasyNeedle(null);
            }
        };
        easyNumbersItem = tableMenu.add(easyNumbersAction);
        easyNumbersItem.setEnabled(false);
        //Create programInfoAction
        Action programInfoAction = new AbstractAction("О программе") {
            @Override
            public void actionPerformed(ActionEvent e) {
                BufferedImage img = null;
                try {
                    img = ImageIO.read (getClass().getResource("KahanDA.gif"));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                Image functionImage = img.getScaledInstance(700, 100, Image.SCALE_SMOOTH);
                ImageIcon image1 = new ImageIcon(img);
                JOptionPane.showMessageDialog(MainFrame.this, "Автор: Каган\nГруппа 9", "О программе", JOptionPane.INFORMATION_MESSAGE, image1);
            }
        };
        aboutProgramItem = aboutProgram.add(programInfoAction);
// Создать новое "действие" по сохранению в текстовый файл
        Action saveToTextAction = new AbstractAction("Сохранить в текстовый файл") {
        public void actionPerformed(ActionEvent event) {
            if (fileChooser==null) {
// Если экземпляр диалогового окна "Открыть файл" ещѐ не создан,
// то создать его
                fileChooser = new JFileChooser();
// и инициализировать текущей директорией
                fileChooser.setCurrentDirectory(new File("."));
            }
// Показать диалоговое окно
            if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION)
// Если результат его показа успешный,
// сохранить данные в текстовый файл
                saveToTextFile(fileChooser.getSelectedFile());
        }
    };
// Добавить соответствующий пункт подменю в меню "Файл"
    saveToTextMenuItem = fileMenu.add(saveToTextAction);
// По умолчанию пункт меню является недоступным (данных ещѐ нет)
saveToTextMenuItem.setEnabled(false);
    // Создать новое "действие" по сохранению в текстовый файл
    Action saveToGraphicsAction = new AbstractAction("Сохранить данные для построения графика") {
    public void actionPerformed(ActionEvent event) {
        if (fileChooser==null) {
// Если экземпляр диалогового окна
// "Открыть файл" ещѐ не создан,
// то создать его
            fileChooser = new JFileChooser();
// и инициализировать текущей директорией
            fileChooser.setCurrentDirectory(new File("."));
        }
// Показать диалоговое окно
        if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION);
// Если результат его показа успешный,
// сохранить данные в двоичный файл
        saveToGraphicsFile(fileChooser.getSelectedFile());
    }
};
// Добавить соответствующий пункт подменю в меню "Файл"
saveToGraphicsMenuItem = fileMenu.add(saveToGraphicsAction);
// По умолчанию пункт меню является недоступным(данных ещѐ нет)
        saveToGraphicsMenuItem.setEnabled(false);
// Создать новое действие по поиску значений многочлена
        Action searchValueAction = new AbstractAction("Найти значение многочлена") {
public void actionPerformed(ActionEvent event) {
// Запросить пользователя ввести искомую строку
        String value = JOptionPane.showInputDialog(MainFrame.this, "Введите значение для поиска", "Поиск значения", JOptionPane.QUESTION_MESSAGE);
// Установить введенное значение в качестве иголки
        renderer.setNeedle(value);
        renderer.setEasyNeedle(null);
// Обновить таблицу
        getContentPane().repaint();
        }
        };
        // Добавить действие в меню "Таблица"
        searchValueMenuItem = tableMenu.add(searchValueAction);
// По умолчанию пункт меню является недоступным (данных ещѐ нет)
        searchValueMenuItem.setEnabled(false);
// Создать область с полями ввода для границ отрезка и шага
// Создать подпись для ввода левой границы отрезка
        JLabel labelForFrom = new JLabel("X изменяется на интервале от:");
// Создать текстовое поле для ввода значения длиной в 10 символов
// со значением по умолчанию 0.0
        textFieldFrom = new JTextField("0.0", 10);
// Установить максимальный размер равный предпочтительному, чтобы
// предотвратить увеличение размера поля ввода
        textFieldFrom.setMaximumSize(textFieldFrom.getPreferredSize());
// Создать подпись для ввода левой границы отрезка
        JLabel labelForTo = new JLabel("до:");
// Создать текстовое поле для ввода значения длиной в 10 символов
// со значением по умолчанию 1.0
        textFieldTo = new JTextField("1.0", 10);
// Установить максимальный размер равный предпочтительному, чтобы
// предотвратить увеличение размера поля ввода
        textFieldTo.setMaximumSize(textFieldTo.getPreferredSize());
// Создать подпись для ввода шага табулирования
        JLabel labelForStep = new JLabel("с шагом:");
// Создать текстовое поле для ввода значения длиной в 10 символов
// со значением по умолчанию 1.0
        textFieldStep = new JTextField("0.1", 10);
// Установить максимальный размер равный предпочтительному, чтобы
// предотвратить увеличение размера поля ввода
        textFieldStep.setMaximumSize(textFieldStep.getPreferredSize());
// Создать контейнер 1 типа "коробка с горизонтальной укладкой"
        Box hboxRange = Box.createHorizontalBox();
// Задать для контейнера тип рамки "объѐмная"
        hboxRange.setBorder(BorderFactory.createBevelBorder(1));
// Добавить "клей" C1-H1
        hboxRange.add(Box.createHorizontalGlue());
// Добавить подпись "От"
        hboxRange.add(labelForFrom);
// Добавить "распорку" C1-H2
        hboxRange.add(Box.createHorizontalStrut(10));
// Добавить поле ввода "От"
        hboxRange.add(textFieldFrom);
// Добавить "распорку" C1-H3
        hboxRange.add(Box.createHorizontalStrut(20));
// Добавить подпись "До"
        hboxRange.add(labelForTo);
// Добавить "распорку" C1-H4
        hboxRange.add(Box.createHorizontalStrut(10));
// Добавить поле ввода "До"
        hboxRange.add(textFieldTo);
// Добавить "распорку" C1-H5
        hboxRange.add(Box.createHorizontalStrut(20));
// Добавить подпись "с шагом"
        hboxRange.add(labelForStep);
// Добавить "распорку" C1-H6
        hboxRange.add(Box.createHorizontalStrut(10));
// Добавить поле для ввода шага табулирования
        hboxRange.add(textFieldStep);
// Добавить "клей" C1-H7
        hboxRange.add(Box.createHorizontalGlue());
// Установить предпочтительный размер области равным удвоенному
// минимальному, чтобы при компоновке область совсем не сдавили
        hboxRange.setPreferredSize(new Dimension(new Double(hboxRange.getMaximumSize().getWidth()).intValue(),
                new Double(hboxRange.getMinimumSize().getHeight()).intValue()*2));
// Установить область в верхнюю (северную) часть компоновки
        getContentPane().add(hboxRange, BorderLayout.NORTH);
// Создать кнопку "Вычислить"
        JButton buttonCalc = new JButton("Вычислить");
// Задать действие на нажатие "Вычислить" и привязать к кнопке
        buttonCalc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
                    renderer.setEasyNeedle(null);
// Считать значения начала и конца отрезка, шага
                    Double from =
                            Double.parseDouble(textFieldFrom.getText());
                    Double to =
                            Double.parseDouble(textFieldTo.getText());
                    Double step =
                            Double.parseDouble(textFieldStep.getText());
// На основе считанных данных создать новый экземпляр модели таблицы
                            data = new GornerTableModel(from, to, step,
                            MainFrame.this.coefficients);
// Создать новый экземпляр таблицы
                    table = new JTable(data);
// Установить в качестве визуализатора ячеек для класса Double разработанный визуализатор
                    table.setDefaultRenderer(Double.class,
                            renderer);
// Установить размер строки таблицы в 30 пикселов
                    table.setRowHeight(30);
// Удалить все вложенные элементы из контейнера hBoxResult
                    hBoxResult.removeAll();
// Добавить в hBoxResult таблицу, "обѐрнутую" в панель с полосами прокрутки
                    hBoxResult.add(new JScrollPane(table));
// Обновить область содержания главного окна
                    getContentPane().validate();
// Пометить ряд элементов меню как доступных
                    saveToCSVItem.setEnabled(true);
                    saveToTextMenuItem.setEnabled(true);
                    saveToGraphicsMenuItem.setEnabled(true);
                    searchValueMenuItem.setEnabled(true);
                    easyNumbersItem.setEnabled(true);
                } catch (NumberFormatException ex) {
// В случае ошибки преобразования чисел показать сообщение об ошибке
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Ошибка в формате записи числа с плавающей точкой", "Ошибочный формат числа",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
// Создать кнопку "Очистить поля"
        JButton buttonReset = new JButton("Очистить поля");
// Задать действие на нажатие "Очистить поля" и привязать к кнопке
        buttonReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
// Установить в полях ввода значения по умолчанию
                textFieldFrom.setText("0.0");
                textFieldTo.setText("1.0");
                textFieldStep.setText("0.1");
// Удалить все вложенные элементы контейнера hBoxResult
                hBoxResult.removeAll();
// Добавить в контейнер пустую панель
                hBoxResult.add(new JPanel());
// Пометить элементы меню как недоступные
                saveToCSVItem.setEnabled(false);
                saveToTextMenuItem.setEnabled(false);
                saveToGraphicsMenuItem.setEnabled(false);
                searchValueMenuItem.setEnabled(false);
                easyNumbersItem.setEnabled(false);
// Обновить область содержания главного окна
                getContentPane().validate();
            }
        });
// Поместить созданные кнопки в контейнер
        Box hboxButtons = Box.createHorizontalBox();
        hboxButtons.setBorder(BorderFactory.createBevelBorder(1));
        hboxButtons.add(Box.createHorizontalGlue());
        hboxButtons.add(buttonCalc);
        hboxButtons.add(Box.createHorizontalStrut(30));
        hboxButtons.add(buttonReset);
        hboxButtons.add(Box.createHorizontalGlue());
// Установить предпочтительный размер области равным удвоенному минимальному, чтобы при
// компоновке окна область совсем не сдавили
        hboxButtons.setPreferredSize(new Dimension(new
                Double(hboxButtons.getMaximumSize().getWidth()).intValue(), new
                Double(hboxButtons.getMinimumSize().getHeight()).intValue()*2));
// Разместить контейнер с кнопками в нижней (южной) области граничной компоновки
        getContentPane().add(hboxButtons, BorderLayout.SOUTH);
// Область для вывода результата пока что пустая
        hBoxResult = Box.createHorizontalBox();
        hBoxResult.add(new JPanel());
// Установить контейнер hBoxResult в главной (центральной) области граничной компоновки
        getContentPane().add(hBoxResult, BorderLayout.CENTER);
    }
    protected void saveToCSV(File selectedFile)
    {
        try {
            FileWriter out  = new FileWriter(selectedFile);
            BufferedWriter output = new BufferedWriter(out);
            /*for (int j = 0; j < data.getColumnCount(); j++) {
                output.write(data.getColumnName(j).toString()+",");
            }*/
            //output.newLine();
                       for (int i = 0; i < table.getRowCount(); i++) {
                for (int j = 0; j < table.getColumnCount(); j++) {
                    output.write(table.getValueAt(i, j).toString()+",");
                }
               output.newLine();
            }
            output.close();
                       out.close();
        }
        catch (Exception ex)
        {

        }
    }
    protected void saveToGraphicsFile(File selectedFile) {
        try {
// Создать новый байтовый поток вывода, направленный в указанный файл
            DataOutputStream out = new DataOutputStream(new
                    FileOutputStream(selectedFile));
// Записать в поток вывода попарно значение X в точке, значение многочлена в точке
            for (int i = 0; i<data.getRowCount(); i++) {
                out.writeDouble((Double)data.getValueAt(i,0));
                out.writeDouble((Double)data.getValueAt(i,1));
            }
// Закрыть поток вывода
            out.close();
        } catch (Exception e) {
// Исключительную ситуацию "ФайлНеНайден" в данном случае можно не обрабатывать,
// так как мы файл создаѐм, а не открываем для чтения
        }
    }
    protected void saveToTextFile(File selectedFile) {
        try {
// Создать новый символьный поток вывода, направленный в указанный файл
            PrintStream out = new PrintStream(selectedFile);
// Записать в поток вывода заголовочные сведения
            out.println("Результаты табулирования многочлена по схеме Горнера");
                    out.print("Многочлен: ");
            for (int i=0; i<coefficients.length; i++) {
                out.print(coefficients[i] + "*X^" +
                        (coefficients.length-i-1));
                if (i!=coefficients.length-1)
                    out.print(" + ");
            }
            out.println("");
            out.println("Интервал от " + data.getFrom() + " до " +
                    data.getTo() + " с шагом " + data.getStep());
            out.println("====================================================");
// Записать в поток вывода значения в точках
            for (int i = 0; i<data.getRowCount(); i++) {
                out.println("Значение в точке " + data.getValueAt(i,0)
                        + " равно " + data.getValueAt(i,1));
            }
// Закрыть поток
            out.close();
        } catch (FileNotFoundException e) {
// Исключительную ситуацию "ФайлНеНайден" можно не
// обрабатывать, так как мы файл создаѐм, а не открываем
        }
    }
    public static void main(String[] args) {
// Если не задано ни одного аргумента командной строки -
// Продолжать вычисления невозможно, коэффиценты неизвестны
        if (args.length==0) {
            System.out.println("Невозможно табулировать многочлен, для которого не задано ни одного коэффициента!");
            System.exit(-1);
        }
// Зарезервировать места в массиве коэффициентов столько, сколько аргументов командной строки
        Double[] coefficients = new Double[args.length];
        int i = 0;
        try {
// Перебрать аргументы, пытаясь преобразовать их в Double
            for (String arg: args) {
                coefficients[i++] = Double.parseDouble(arg);
            }
        }
        catch (NumberFormatException ex) {
// Если преобразование невозможно - сообщить об ошибке и завершиться
            System.out.println("Ошибка преобразования строки '" +  args[i] + "' в число типа Double");
            System.exit(-2);
        }
// Создать экземпляр главного окна, передав ему коэффициенты
        MainFrame frame = new MainFrame(coefficients);
// Задать действие, выполняемое при закрытии окна
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}