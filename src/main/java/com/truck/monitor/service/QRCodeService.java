package com.truck.monitor.service;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * 二维码工具类.
 * 
 * @author vince.liu
 *
 */
@Component
public class QRCodeService {

	@Value("${user.dir}")
	private String dir;

	private final String CHARSET = "utf-8";

	private final String FORMAT_NAME = "JPG";
	// 二维码尺寸
	private final int QRCODE_SIZE = 400;
	// LOGO宽度
	private final int WIDTH = 60;
	// LOGO高度
	private final int HEIGHT = 60;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private BufferedImage createImage(String content, String imgUrl, Boolean needCompress) throws Exception {
		Hashtable hints = new Hashtable();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
		hints.put(EncodeHintType.MARGIN, 1);
		BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE, hints);
		int width = bitMatrix.getWidth();
		int height = bitMatrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
			}
		}
		if (StringUtils.isEmpty(imgUrl)) {
			return image;
		}
		// 插入图片
		insertImage(image, imgUrl, needCompress);
		return image;
	}

	/**
	 * 插入LOGO
	 * 
	 * @param source
	 *            二维码图片
	 * @param imgUrl
	 *            LOGO图片地址
	 * @param needCompress
	 *            是否压缩
	 * @throws Exception
	 */
	private void insertImage(BufferedImage source, String imgUrl, Boolean needCompress) throws Exception {

		Image src = ImageIO.read(new URL(imgUrl).openStream());
		int width = src.getWidth(null);
		int height = src.getHeight(null);
		if (needCompress != null && needCompress) { // 压缩LOGO
			if (width > WIDTH) {
				width = WIDTH;
			}
			if (height > HEIGHT) {
				height = HEIGHT;
			}
			Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			g.drawImage(image, 0, 0, null); // 绘制缩小后的图
			g.dispose();
			src = image;
		}
		// 插入LOGO
		Graphics2D graph = source.createGraphics();
		int x = (QRCODE_SIZE - width) / 2;
		int y = (QRCODE_SIZE - height) / 2;
		graph.drawImage(src, x, y, width, height, null);
		Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
		graph.setStroke(new BasicStroke(3f));
		graph.draw(shape);
		graph.dispose();
	}

	/**
	 * 生成二维码(内嵌LOGO)
	 * 
	 * @param content
	 *            内容
	 * @param imgUrl
	 *            LOGO地址
	 * @param dest
	 *            存放目录
	 * @param needCompress
	 *            是否压缩LOGO
	 * @throws Exception
	 */
	public File encode(String content, String imgUrl, Boolean needCompress) throws Exception {
		String dest = getDir() + getStoragePath();
		mkdirs(dest);
		BufferedImage image = createImage(content, imgUrl, needCompress);
		File qrCode = new File(dest + "/" + UUID.randomUUID().toString() + ".jpg");
		ImageIO.write(image, FORMAT_NAME, qrCode);
		return qrCode;
	}

	/**
	 * 当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
	 * 
	 * @param dest
	 *            存放目录
	 */
	public void mkdirs(String dest) {
		File file = new File(dest);
		if (!file.exists() && !file.isDirectory()) {
			file.mkdirs();
		}
	}

	public String getStoragePath() {
		Calendar cal = Calendar.getInstance();
		StringBuilder storagePath = new StringBuilder().append(java.io.File.separator).append("upload").append(java.io.File.separator).append(cal.get(Calendar.YEAR))
				.append(java.io.File.separator).append((cal.get(Calendar.MONTH) + 1)).append(java.io.File.separator).append(cal.get(Calendar.DAY_OF_MONTH));
		return storagePath.toString();
	}

	public String getDir() {
		return dir;
	}

}
