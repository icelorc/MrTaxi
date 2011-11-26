package edu.cs.nccu.rrtaxi.tabActivity;

import android.os.Bundle;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import java.net.HttpURLConnection;
import org.w3c.dom.*;
import java.net.URL;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import java.net.MalformedURLException;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import android.graphics.Color;

import edu.cs.nccu.rrtaxi.R;

public class map extends MapActivity {
	/** Called when the activity is first created. */
	MapView mapView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		
		MapView mapView = (MapView) findViewById(R.id.myMapView1); 
		
		double src_lat = 25.04202; // the testing source 
		double src_long = 121.534761; 
		double dest_lat = 25.05202; // the testing destination 
		double dest_long = 121.554761; 
		GeoPoint srcGeoPoint = new GeoPoint((int) (src_lat * 1E6), 
				(int) (src_long * 1E6)); 
		GeoPoint destGeoPoint = new GeoPoint((int) (dest_lat * 1E6), 
				(int) (dest_long * 1E6)); 

		DrawPath(srcGeoPoint, destGeoPoint, Color.GREEN, mapView); 

		mapView.getController().animateTo(srcGeoPoint); 
		mapView.getController().setZoom(15);
	}


	
		private void DrawPath(GeoPoint src, GeoPoint dest,int color, MapView myMapView1){
		//connect to web server
		StringBuilder urlString = new StringBuilder();
		urlString.append("http://maps.google.com/maps?f=d&hl=en");
		urlString.append("&saddr=");//from 
		urlString.append( Double.toString((double)src.getLatitudeE6()/1.0E6 ));
		urlString.append(","); 
		urlString.append( Double.toString((double)src.getLongitudeE6()/1.0E6 ));
		urlString.append("&daddr=");//to 
		urlString.append( Double.toString((double)dest.getLatitudeE6()/1.0E6 )); 
		urlString.append(","); 
		urlString.append( Double.toString((double)dest.getLongitudeE6()/1.0E6 ));
		urlString.append("&ie=UTF8&0&om=0&output=kml"); 
		Document doc = null;
		HttpURLConnection urlConnection= null;
		URL url = null;
		try
		{
			url = new URL(urlString.toString());
			urlConnection=(HttpURLConnection)url.openConnection(); 
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(true); 
			urlConnection.setDoInput(true); 
			urlConnection.connect(); 

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(urlConnection.getInputStream());
			if(doc.getElementsByTagName("GeometryCollection").getLength()>0)
			{
				//String path = doc.getElementsByTagName("GeometryCollection").item(0).getFirstChild().getFirstChild().getNodeName();
				String path = doc.getElementsByTagName("GeometryCollection").item(0).getFirstChild().getFirstChild().getFirstChild().getNodeValue();
				String [] pairs = path.split(" ");
				String[] lngLat = pairs[0].split(","); // lngLat[0]=longitude lngLat[1]=latitude lngLat[2]=height
				//src
				GeoPoint startGP = new GeoPoint((int)(Double.parseDouble(lngLat[1])*1E6),(int)(Double.parseDouble(lngLat[0])*1E6));
				myMapView1.getOverlays().add(new MyOverLay(startGP,startGP,1));
				GeoPoint gp1;
				GeoPoint gp2 = startGP; 
				for(int i=1;i<pairs.length;i++) // the last one would be crash
				{ 
					lngLat = pairs[i].split(","); 
					gp1 = gp2; 
					// watch out! For GeoPoint, first:latitude, second:longitude 
					gp2 = new GeoPoint((int)(Double.parseDouble(lngLat[1])*1E6),(int)(Double.parseDouble(lngLat[0])*1E6)); 
					myMapView1.getOverlays().add(new MyOverLay(gp1,gp2,2,color));
				}
				myMapView1.getOverlays().add(new MyOverLay(dest,dest, 3)); // use the default color 
			}
		}
		catch (MalformedURLException e) 
		{ 
			e.printStackTrace(); 
		}
		catch (IOException e) 
		{ 
			e.printStackTrace(); 
		}
		catch (ParserConfigurationException e) 
		{ 
			e.printStackTrace(); 
		}
		catch (SAXException e) 
		{ 
			e.printStackTrace(); 
		} 
	}



		@Override
		protected boolean isRouteDisplayed() {
			// TODO Auto-generated method stub
			return false;
		}
}
