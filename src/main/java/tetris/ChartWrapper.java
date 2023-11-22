package tetris;

import static com.googlecode.charts4j.Color.BLACK;
import static com.googlecode.charts4j.Color.LIGHTYELLOW;
import static com.googlecode.charts4j.Color.WHITE;
import static com.googlecode.charts4j.Color.YELLOW;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.googlecode.charts4j.AxisLabels;
import com.googlecode.charts4j.AxisLabelsFactory;
import com.googlecode.charts4j.AxisStyle;
import com.googlecode.charts4j.AxisTextAlignment;
import com.googlecode.charts4j.Color;
import com.googlecode.charts4j.DataUtil;
import com.googlecode.charts4j.Fills;
import com.googlecode.charts4j.GCharts;
import com.googlecode.charts4j.Line;
import com.googlecode.charts4j.LineChart;
import com.googlecode.charts4j.LineStyle;
import com.googlecode.charts4j.Plots;
import com.googlecode.charts4j.Shape;

public class ChartWrapper {

	public static void createChart(double scores[]) {
		if (Main.DEBUG) {
			System.out.println("============CHART============");
			for (double d : scores) {
				System.out.print(d + " ");
			}
			System.out.println();
		}
		
		Line line1 = Plots.newLine(DataUtil.scaleWithinRange(-20, 50, scores), YELLOW, "Score");
		line1.setLineStyle(LineStyle.newLineStyle(3, 1, 0));
		line1.addShapeMarkers(Shape.CIRCLE, YELLOW, 10);
		line1.addShapeMarkers(Shape.CIRCLE, BLACK, 7);
		line1.setFillAreaColor(LIGHTYELLOW);

		// Defining chart.
		LineChart chart = GCharts.newLineChart(line1);
		chart.setSize(600, 450);
		chart.setTitle("Sarsa score evolution", WHITE, 14);

		// Defining axis info and styles
		AxisStyle axisStyle = AxisStyle.newAxisStyle(WHITE, 12, AxisTextAlignment.CENTER);
		AxisLabels yAxis = AxisLabelsFactory.newNumericRangeAxisLabels(-20, 60);
		yAxis.setAxisStyle(axisStyle);
		AxisLabels xAxis2 = AxisLabelsFactory.newNumericRangeAxisLabels(0, Main.TRAINING_ROUNDS
				* Main.GAMES_PER_TRAINING_ROUND);
		xAxis2.setAxisStyle(axisStyle);
		AxisLabels xAxis3 = AxisLabelsFactory.newAxisLabels("Games", 50.0);
		xAxis3.setAxisStyle(AxisStyle.newAxisStyle(WHITE, 14, AxisTextAlignment.CENTER));

		// Adding axis info to chart.
		chart.addYAxisLabels(yAxis);
		chart.addXAxisLabels(xAxis2);
		chart.addXAxisLabels(xAxis3);
		chart.setGrid(100, 6.78, 5, 0);

		// Defining background and chart fills.
		chart.setBackgroundFill(Fills.newSolidFill(BLACK));
		chart.setAreaFill(Fills.newSolidFill(Color.newColor("708090")));
		try {
			displayUrlString(chart.toURLString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void displayUrlString(final String urlString) throws IOException {
		JFrame frame = new JFrame();
		JLabel label = new JLabel(new ImageIcon(ImageIO.read(new URL(urlString))));
		frame.getContentPane().add(label, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}

}
