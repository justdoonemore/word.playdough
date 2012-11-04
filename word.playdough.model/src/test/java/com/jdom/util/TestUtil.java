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

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Ignore;

@Ignore
public class TestUtil {

	private static final File JAVA_IO_TMPDIR = new File(
			System.getProperty("java.io.tmpdir"));

	private TestUtil() {

	}

	public static File getTestClassDir(Class<?> testClass) {
		File testDir = new File(JAVA_IO_TMPDIR, testClass.getSimpleName());
		if (testDir.exists()) {
			try {
				FileUtils.deleteDirectory(testDir);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}
		}

		return testDir;
	}

}
