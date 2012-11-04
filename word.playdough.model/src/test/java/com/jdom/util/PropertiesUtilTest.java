/** 
 *  Copyright (C) 2012  Just Do One More
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.jdom.util;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.jdom.util.PropertiesUtil;
import com.jdom.word.playdough.model.GamePackListModelTest;

public class PropertiesUtilTest {
	@Test
	public void testWritesOutPropertiesToSpecifiedLocation() throws IOException {
		Properties properties = new Properties();
		properties.setProperty("key", "value");

		File testOutputFile = new File(
				GamePackListModelTest.setupTestClassDir(getClass()),
				"output.properties");

		PropertiesUtil.writePropertiesFile(properties, testOutputFile);

		String output = FileUtils.readFileToString(testOutputFile);

		assertTrue("Did not find the property set in the output file!",
				output.contains("key=value"));
	}
}