/**
 * Copyright (C) 2013-2014 Olaf Lessenich
 * Copyright (C) 2014-2015 University of Passau, Germany
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 *
 * Contributors:
 *     Olaf Lessenich <lessenic@fim.uni-passau.de>
 *     Georg Seibt <seibt@fim.uni-passau.de>
 */
package de.fosd.jdime;

import java.io.File;
import java.nio.file.Files;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.fosd.jdime.common.ArtifactList;
import de.fosd.jdime.common.FileArtifact;
import de.fosd.jdime.common.MergeContext;
import de.fosd.jdime.strategy.MergeStrategy;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * @author Olaf Lessenich
 *
 */
public class MergeTest {

    private MergeContext context;
    private static final String[] STRATEGIES = { "linebased", "structured",
            "combined" };

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        // initialize logger
        Logger root = Logger.getLogger(JDimeWrapper.class.getPackage().getName());
        root.setLevel(Level.WARNING);

        for (Handler handler : root.getHandlers()) {
            handler.setLevel(Level.WARNING);
        }

        // initialize context
        context = new MergeContext();
        context.setQuiet(true);
        context.setPretend(false);
    }

    private final void runMerge(String filepath, boolean threeway) {
        try {
            // initialize input files
            ArtifactList<FileArtifact> inputArtifacts = new ArtifactList<>();
            inputArtifacts.add(new FileArtifact(new File("testfiles/left/"
                    + filepath)));
            if (threeway) {
                inputArtifacts.add(new FileArtifact(new File("testfiles/base/"
                        + filepath)));
            }
            inputArtifacts.add(new FileArtifact(new File("testfiles/right/"
                    + filepath)));

            for (String strategy : STRATEGIES) {
                // setup context
                context.setMergeStrategy(MergeStrategy.parse(strategy));
                context.setInputFiles(inputArtifacts);
                File out = Files.createTempFile("jdime-tests", ".java").toFile();
                out.deleteOnExit();
                context.setOutputFile(new FileArtifact(out));

                // run
                System.out.println("Running " + strategy + " strategy on "
                        + filepath);
                Main.merge(context);
                
                // check
                File expected = new File("testfiles" + File.separator
                        + strategy + File.separator + filepath);
                System.out.println("----------Expected:-----------");
                System.out.print(FileUtils.readFileToString(expected));
                System.out.println("----------Received:-----------");
                System.out.print(context.getOutputFile().getContent());
                System.out.println("------------------------------");
                assertTrue("Strategy " + strategy
                        + " resulted in unexpected output",
                        FileUtils.contentEquals(context.getOutputFile()
                                .getFile(), expected));
                System.out.println();
            }
        } catch (Exception e) {
            fail(e.toString());
        }
    }

    @Test
    public final void testBag() {
        runMerge("SimpleTests/Bag/Bag.java", true);
    }

    @Test
    public final void testBag2() {
        runMerge("SimpleTests/Bag/Bag2.java", true);
    }

    @Test
    public final void testBag3() {
        runMerge("SimpleTests/Bag/Bag3.java", true);
    }
    
    @Test
    public final void testImportConflict () {
        runMerge("SimpleTests/ImportMess.java", true);
    }

    @Test
    public final void testExprTest () {
        runMerge("SimpleTests/ExprTest.java", true);
    }
}
