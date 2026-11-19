package com.example.data

object SeedData {
    val initialPassages = listOf(
        // ==================== CLASS 10 SCIENCE: CHEMISTRY ====================
        TextbookPassage(
            textbookName = "Class 10 Science",
            educationLevel = "Class 10",
            subject = "Science (Chemistry)",
            chapterName = "Ch 1: Chemical Reactions and Equations",
            pageNumber = 6,
            content = "A chemical reaction involves breaking and making of bonds between atoms to produce new substances. Balanced chemical equations obey the Law of Conservation of Mass. Main types: Combination (A + B -> AB), Decomposition (AB -> A + B, thermal/electrolytic/photolytic), Displacement (A + BC -> AC + B), Double Displacement (precipitation), and Redox (Oxidation = gain of oxygen/loss of hydrogen; Reduction = loss of oxygen/gain of hydrogen). Corrosion (rusting of iron) and Rancidity (oxidation of fats/oils) are everyday oxidation effects.",
            realLifeExample = "Iron gates rusting in monsoon rain is corrosion; chips packets are flushed with nitrogen gas to prevent rancidity; burning magnesium ribbon forms white magnesium oxide ash.",
            formulaOrEquation = "2Mg + O2 -> 2MgO  |  CaO + H2O -> Ca(OH)2 + Heat",
            practiceQuestion = "Why is respiration considered an exothermic chemical reaction?"
        ),
        TextbookPassage(
            textbookName = "Class 10 Science",
            educationLevel = "Class 10",
            subject = "Science (Chemistry)",
            chapterName = "Ch 2: Acids, Bases and Salts",
            pageNumber = 18,
            content = "Acids are sour, turn blue litmus red, and produce H+ ions in water. Bases are bitter, soapy, turn red litmus blue, and produce OH- ions in water. Neutralization: Acid + Base -> Salt + Water. The pH scale runs 0 to 14 (pH < 7 acidic, pH = 7 neutral, pH > 7 basic). Important salts: Bleaching Powder (CaOCl2), Baking Soda (NaHCO3), Washing Soda (Na2CO3·10H2O), and Plaster of Paris (CaSO4·½H2O).",
            realLifeExample = "Farmers add slaked lime to neutralize acidic soil; baking soda relieves ant stings (formic acid); Plaster of Paris sets fractured bones.",
            formulaOrEquation = "HCl + NaOH -> NaCl + H2O  |  CaSO4·2H2O (373K) -> CaSO4·½H2O + 1½H2O",
            practiceQuestion = "Why should Plaster of Paris be stored in a moisture-proof container?"
        ),
        TextbookPassage(
            textbookName = "Class 10 Science",
            educationLevel = "Class 10",
            subject = "Science (Chemistry)",
            chapterName = "Ch 3: Metals and Non-metals",
            pageNumber = 37,
            content = "Metals are lustrous, malleable, ductile, sonorous, and good conductors of heat and electricity (exceptions: Mercury is liquid, Sodium/Potassium are soft). Non-metals are brittle and poor conductors (exception: Graphite conducts electricity). Reactivity series orders metals from most reactive (K, Na, Ca, Mg, Al, Zn, Fe, Pb) to least reactive (Cu, Hg, Ag, Au). Ionic compounds form by electron transfer, have high melting points, and conduct electricity in molten or aqueous state. Metallurgy extracts metals via roasting (sulphide ores) and calcination (carbonate ores).",
            realLifeExample = "Copper and aluminium wires carry electricity; galvanization (zinc coating) prevents iron pipes from rusting; alloys like brass (Cu+Zn) and stainless steel resist corrosion.",
            formulaOrEquation = "2ZnS + 3O2 -> 2ZnO + 2SO2 (Roasting)  |  ZnCO3 -> ZnO + CO2 (Calcination)",
            practiceQuestion = "Why are sodium and potassium stored immersed in kerosene oil?"
        ),
        TextbookPassage(
            textbookName = "Class 10 Science",
            educationLevel = "Class 10",
            subject = "Science (Chemistry)",
            chapterName = "Ch 4: Carbon and its Compounds",
            pageNumber = 58,
            content = "Carbon forms covalent bonds due to tetravalency and shows catenation (self-linking into long chains and rings). Hydrocarbons are saturated (alkanes, CnH2n+2, single bond) or unsaturated (alkenes CnH2n double bond, alkynes CnH2n-2 triple bond). Functional groups: Alcohol (-OH), Aldehyde (-CHO), Ketone (-CO-), Carboxylic acid (-COOH). Homologous series differs by a -CH2- unit. Soaps are sodium/potassium salts of long-chain carboxylic acids that clean via micelle formation; detergents work even in hard water.",
            realLifeExample = "Ethanol is used in medicines and tincture of iodine; ethanoic acid (5-8% in water) is vinegar used in pickles; soap micelles trap oil and dirt when washing clothes.",
            formulaOrEquation = "CH3COOH + C2H5OH --(Acid)--> CH3COOC2H5 (Ester) + H2O",
            practiceQuestion = "Why does soap not form lather easily in hard water, whereas detergents do?"
        ),

        // ==================== CLASS 10 SCIENCE: BIOLOGY ====================
        TextbookPassage(
            textbookName = "Class 10 Science",
            educationLevel = "Class 10",
            subject = "Science (Biology)",
            chapterName = "Ch 5: Life Processes",
            pageNumber = 93,
            content = "Life processes include Nutrition, Respiration, Transportation, and Excretion. Photosynthesis is autotrophic nutrition in green plants using CO2, H2O, sunlight, and chlorophyll to form glucose and O2. Human digestion starts in the mouth (salivary amylase), continues in the stomach (HCl, pepsin) and small intestine (bile, pancreatic juice, villi absorption). Aerobic respiration breaks glucose with oxygen into CO2, H2O, and 38 ATP; anaerobic respiration produces lactic acid (muscles) or ethanol (yeast). The 4-chambered human heart performs double circulation. Xylem transports water; phloem translocates food. Nephrons in kidneys filter blood to form urine.",
            realLifeExample = "Green paddy leaves make food via photosynthesis; athletes feel muscle cramps during heavy running due to lactic acid from anaerobic respiration.",
            formulaOrEquation = "6CO2 + 6H2O --(Sunlight/Chlorophyll)--> C6H12O6 + 6O2",
            practiceQuestion = "What is the role of villi in the small intestine and alveoli in the lungs?"
        ),
        TextbookPassage(
            textbookName = "Class 10 Science",
            educationLevel = "Class 10",
            subject = "Science (Biology)",
            chapterName = "Ch 6: Control and Coordination",
            pageNumber = 114,
            content = "In animals, control and coordination occur via the Nervous System (neurons, synapse, reflex arc in spinal cord, brain: forebrain/cerebrum for thinking, cerebellum for posture/balance, medulla for involuntary actions) and Endocrine System (hormones: adrenaline for fight-or-flight, thyroxine for metabolism, insulin for blood sugar, growth hormone). Plants coordinate via tropic movements (phototropism, geotropism, hydrotropism, chemotropism) and plant hormones: Auxin (cell elongation), Gibberellin (stem growth), Cytokinin (cell division), and Abscisic acid (growth inhibition/wilting).",
            realLifeExample = "Pulling your hand away instantly from a hot pan is a reflex action; sunflower shoots bending toward sunlight is positive phototropism driven by auxin.",
            formulaOrEquation = "Stimulus -> Receptor -> Sensory Neuron -> Spinal Cord -> Motor Neuron -> Effector",
            practiceQuestion = "Why is iodised salt advisable in our daily diet?"
        ),
        TextbookPassage(
            textbookName = "Class 10 Science",
            educationLevel = "Class 10",
            subject = "Science (Biology)",
            chapterName = "Ch 7: How do Organisms Reproduce?",
            pageNumber = 127,
            content = "Reproduction ensures continuity of species and DNA copying introduces variations. Asexual modes: Fission (Amoeba binary, Plasmodium multiple), Fragmentation (Spirogyra), Regeneration (Planaria), Budding (Hydra, Yeast), Vegetative Propagation (bryophyllum, grafting), Spore Formation (Rhizopus). Sexual reproduction in flowering plants involves pollination (self/cross), fertilization in the ovule, seed formation, and fruit development. In humans, male gametes (sperm) fertilize female egg in the fallopian tube; the embryo develops in the uterus nourished via the placenta.",
            realLifeExample = "Growing sugarcane or rose plants from stem cuttings is vegetative propagation; bees transferring pollen between flowers enable fruit and crop production.",
            formulaOrEquation = "Male Gamete (n) + Female Gamete (n) -> Zygote (2n) -> Embryo",
            practiceQuestion = "What are the advantages of vegetative propagation for farmers?"
        ),
        TextbookPassage(
            textbookName = "Class 10 Science",
            educationLevel = "Class 10",
            subject = "Science (Biology)",
            chapterName = "Ch 8: Heredity",
            pageNumber = 142,
            content = "Heredity is the transmission of traits from parents to offspring via genes on chromosomes. Gregor Mendel's pea plant experiments established: Law of Dominance, Law of Segregation (monohybrid F2 phenotypic ratio 3:1, genotypic 1:2:1), and Law of Independent Assortment (dihybrid F2 ratio 9:3:3:1). In humans, sex determination depends on the father's sperm: XX produces a female child and XY produces a male child (50% probability each).",
            realLifeExample = "Tall (TT) and dwarf (tt) pea plants produce all tall (Tt) plants in F1, and 3 tall : 1 dwarf in F2.",
            formulaOrEquation = "Monohybrid F2 = 3:1  |  Dihybrid F2 = 9:3:3:1  |  XX (Female), XY (Male)",
            practiceQuestion = "How is the sex of a newborn child determined in human beings?"
        ),
        TextbookPassage(
            textbookName = "Class 10 Science",
            educationLevel = "Class 10",
            subject = "Science (Biology)",
            chapterName = "Ch 13: Our Environment",
            pageNumber = 256,
            content = "An ecosystem consists of biotic (producers, consumers, decomposers) and abiotic components. Food chains show unidirectional energy flow following the 10% Law: only 10% of energy transfers from one trophic level to the next. Biological magnification is the progressive accumulation of non-biodegradable pesticides like DDT at higher trophic levels. The ozone layer (O3) in the stratosphere shields Earth from harmful UV radiation; CFCs deplete ozone. Biodegradable waste decomposes naturally; non-biodegradable plastics persist and pollute.",
            realLifeExample = "Grass -> Grasshopper -> Frog -> Snake -> Hawk is a food chain; segregating wet kitchen waste for compost and dry plastic for recycling protects village soil.",
            formulaOrEquation = "O2 --(UV)--> O + O  |  O + O2 -> O3 (Ozone)",
            practiceQuestion = "Why do food chains generally have only 3 or 4 trophic levels?"
        ),

        // ==================== CLASS 10 SCIENCE: PHYSICS ====================
        TextbookPassage(
            textbookName = "Class 10 Science",
            educationLevel = "Class 10",
            subject = "Science (Physics)",
            chapterName = "Ch 9: Light — Reflection and Refraction",
            pageNumber = 160,
            content = "Laws of Reflection: angle of incidence equals angle of reflection (i = r). Concave mirrors converge light (used in torches, headlights, dentists' mirrors); convex mirrors always form virtual, erect, diminished images (used as rear-view mirrors). Refraction is bending of light across media obeying Snell's Law (sin i / sin r = n). Convex lenses converge light (magnifying glass); concave lenses diverge light. Power of a lens P = 1/f (in metres), unit Dioptre (D).",
            realLifeExample = "A pencil dipped in a glass of water appears bent due to refraction; vehicle side mirrors are convex to give a wider field of view.",
            formulaOrEquation = "Mirror: 1/v + 1/u = 1/f  |  Lens: 1/v - 1/u = 1/f  |  P = 1/f (D)",
            practiceQuestion = "Find the focal length of a lens of power -2.0 D. What type of lens is this?"
        ),
        TextbookPassage(
            textbookName = "Class 10 Science",
            educationLevel = "Class 10",
            subject = "Science (Physics)",
            chapterName = "Ch 10: The Human Eye and the Colourful World",
            pageNumber = 187,
            content = "The human eye focuses light onto the retina using the crystalline lens (power of accommodation; near point 25 cm, far point infinity). Vision defects: Myopia (near-sightedness, corrected by concave lens), Hypermetropia (far-sightedness, corrected by convex lens), Presbyopia (age-related, bifocal lens). A glass prism disperses white light into VIBGYOR. Atmospheric refraction causes twinkling of stars and advance sunrise; Tyndall effect and scattering of shorter blue wavelengths make the sky appear blue.",
            realLifeExample = "A rainbow forms after rain due to dispersion, refraction, and internal reflection of sunlight inside water droplets; danger signals are red because red scatters the least.",
            formulaOrEquation = "VIBGYOR (Violet bends most, Red bends least)",
            practiceQuestion = "Why do stars twinkle while planets do not?"
        ),
        TextbookPassage(
            textbookName = "Class 10 Science",
            educationLevel = "Class 10",
            subject = "Science (Physics)",
            chapterName = "Ch 11: Electricity",
            pageNumber = 200,
            content = "Electric current I = Q/t (Ampere). Potential difference V = W/Q (Volt). Ohm's Law: V = IR at constant temperature. Resistance R = ρ(L/A) depends on length, area, material, and temperature. Resistors in series: Rs = R1 + R2 + R3 (same current). Resistors in parallel: 1/Rp = 1/R1 + 1/R2 + 1/R3 (same voltage). Joule's Law of Heating: H = I²Rt. Electric Power P = VI = I²R = V²/R; commercial unit of energy is 1 kWh = 3.6 × 10⁶ J.",
            realLifeExample = "Household appliances are connected in parallel so each gets 220 V and works independently; electric irons and fuses use the heating effect of current.",
            formulaOrEquation = "V = IR  |  H = I²Rt  |  P = VI  |  1 kWh = 3.6 × 10⁶ J",
            practiceQuestion = "Why are alloys like nichrome used in electric toaster heating coils instead of pure metals?"
        ),
        TextbookPassage(
            textbookName = "Class 10 Science",
            educationLevel = "Class 10",
            subject = "Science (Physics)",
            chapterName = "Ch 12: Magnetic Effects of Electric Current",
            pageNumber = 224,
            content = "A current-carrying conductor produces a magnetic field around it (Right-Hand Thumb Rule). Magnetic field lines emerge from North pole and merge at South pole and never intersect. A solenoid acts like a bar magnet and forms an electromagnet with a soft iron core. Fleming's Left-Hand Rule gives the direction of force on a current-carrying conductor in a magnetic field (principle of electric motors). Domestic circuits use live (red/brown), neutral (black/blue), and earth (green) wires with fuses/MCBs to prevent overloading and short-circuiting.",
            realLifeExample = "Electric fans, water pumps, and mixers use motors based on magnetic force; the green earth wire protects users from electric shock in metal appliances.",
            formulaOrEquation = "Right-Hand Thumb Rule (Field)  |  Fleming's Left-Hand Rule (Force)",
            practiceQuestion = "Why do two magnetic field lines never intersect each other?"
        ),

        // ==================== CLASS 10 MATHEMATICS ====================
        TextbookPassage(
            textbookName = "Class 10 Mathematics",
            educationLevel = "Class 10",
            subject = "Mathematics",
            chapterName = "Ch 1 & 2: Real Numbers and Polynomials",
            pageNumber = 8,
            content = "Fundamental Theorem of Arithmetic: Every composite number can be uniquely expressed as a product of primes. For any two positive integers a and b, HCF(a, b) × LCM(a, b) = a × b. Irrational numbers like √2, √3, √5 cannot be written as p/q. For a quadratic polynomial ax² + bx + c with zeroes α and β: Sum of zeroes (α + β) = -b/a and Product of zeroes (αβ) = c/a.",
            realLifeExample = "Finding the maximum number of students per row for equal marching columns uses HCF; finding when two village buses meet again at the stand uses LCM.",
            formulaOrEquation = "HCF × LCM = a × b  |  α + β = -b/a  |  αβ = c/a",
            practiceQuestion = "If HCF(306, 657) = 9, find LCM(306, 657)."
        ),
        TextbookPassage(
            textbookName = "Class 10 Mathematics",
            educationLevel = "Class 10",
            subject = "Mathematics",
            chapterName = "Ch 3 & 4: Linear & Quadratic Equations",
            pageNumber = 52,
            content = "A pair of linear equations a1x + b1y + c1 = 0 and a2x + b2y + c2 = 0 has: unique solution if a1/a2 != b1/b2 (intersecting), infinitely many if a1/a2 = b1/b2 = c1/c2 (coincident), and no solution if a1/a2 = b1/b2 != c1/c2 (parallel). Solved by substitution or elimination. A quadratic equation ax² + bx + c = 0 (a != 0) is solved by factorization or the Quadratic Formula. Discriminant D = b² - 4ac determines nature of roots (D > 0 two distinct real, D = 0 two equal real, D < 0 no real roots).",
            realLifeExample = "Calculating rectangular farm dimensions when area and perimeter are known leads directly to a quadratic equation.",
            formulaOrEquation = "x = (-b ± √(b² - 4ac)) / 2a  |  D = b² - 4ac",
            practiceQuestion = "Find the roots of x² - 5x + 6 = 0 and verify the discriminant."
        ),
        TextbookPassage(
            textbookName = "Class 10 Mathematics",
            educationLevel = "Class 10",
            subject = "Mathematics",
            chapterName = "Ch 5: Arithmetic Progressions (AP)",
            pageNumber = 95,
            content = "An Arithmetic Progression (AP) is a list of numbers where each term is obtained by adding a fixed common difference (d) to the preceding term: a, a+d, a+2d, ... The nth term is an = a + (n - 1)d. The sum of the first n terms is Sn = (n/2)[2a + (n - 1)d] or Sn = (n/2)(a + l), where l is the last term.",
            realLifeExample = "Saving ₹100 in week 1, ₹120 in week 2, ₹140 in week 3 forms an AP with a = 100 and d = 20.",
            formulaOrEquation = "an = a + (n - 1)d  |  Sn = (n/2)[2a + (n - 1)d]",
            practiceQuestion = "Find the 10th term of the AP: 2, 7, 12, ..."
        ),
        TextbookPassage(
            textbookName = "Class 10 Mathematics",
            educationLevel = "Class 10",
            subject = "Mathematics",
            chapterName = "Ch 6 & 7: Triangles and Coordinate Geometry",
            pageNumber = 124,
            content = "Basic Proportionality Theorem (Thales Theorem): A line drawn parallel to one side of a triangle divides the other two sides in the same ratio. Similarity criteria: AAA, SAS, SSS. In Coordinate Geometry, Distance Formula between (x1, y1) and (x2, y2) is √[(x2 - x1)² + (y2 - y1)²]. Section Formula for internal division in ratio m1:m2 is ((m1x2 + m2x1)/(m1+m2), (m1y2 + m2y1)/(m1+m2)). Midpoint is ((x1+x2)/2, (y1+y2)/2).",
            realLifeExample = "Measuring the height of a tall tree using its shadow length and a stick's shadow uses similar triangles.",
            formulaOrEquation = "d = √[(x2-x1)² + (y2-y1)²]  |  Midpoint = ((x1+x2)/2, (y1+y2)/2)",
            practiceQuestion = "Find the distance between points (2, 3) and (4, 1)."
        ),
        TextbookPassage(
            textbookName = "Class 10 Mathematics",
            educationLevel = "Class 10",
            subject = "Mathematics",
            chapterName = "Ch 8 & 9: Trigonometry & Heights and Distances",
            pageNumber = 175,
            content = "In a right triangle: sin θ = Opposite/Hypotenuse, cos θ = Adjacent/Hypotenuse, tan θ = Opposite/Adjacent = sin θ / cos θ. Standard angles: sin 0°=0, sin 30°=1/2, sin 45°=1/√2, sin 60°=√3/2, sin 90°=1. Fundamental identities: sin²θ + cos²θ = 1, 1 + tan²θ = sec²θ, 1 + cot²θ = cosec²θ. Heights and distances use angle of elevation (looking up) and angle of depression (looking down).",
            realLifeExample = "Surveyors find the height of a mobile tower or river width without climbing or crossing it using tan θ and angle of elevation.",
            formulaOrEquation = "sin²θ + cos²θ = 1  |  1 + tan²θ = sec²θ  |  1 + cot²θ = cosec²θ",
            practiceQuestion = "If a tower casts a shadow equal to its height, what is the angle of elevation of the Sun?"
        ),
        TextbookPassage(
            textbookName = "Class 10 Mathematics",
            educationLevel = "Class 10",
            subject = "Mathematics",
            chapterName = "Ch 10, 11 & 12: Circles, Areas & Surface Volumes",
            pageNumber = 208,
            content = "A tangent to a circle is perpendicular to the radius at the point of contact, and lengths of two tangents drawn from an external point to a circle are equal. Area of a sector of angle θ = (θ/360) × πr²; arc length = (θ/360) × 2πr. Surface Areas & Volumes: Cylinder (CSA = 2πrh, Vol = πr²h), Cone (CSA = πrl, Vol = ⅓πr²h), Sphere (SA = 4πr², Vol = 4/3πr³), Hemisphere (CSA = 2πr², TSA = 3πr², Vol = ⅔πr³).",
            realLifeExample = "Calculating how many litres of water a cylindrical village tank holds uses Volume = πr²h.",
            formulaOrEquation = "Sector = (θ/360)πr²  |  Cylinder V = πr²h  |  Cone V = ⅓πr²h  |  Sphere V = (4/3)πr³",
            practiceQuestion = "Two tangents from an external point P to a circle touch at A and B. If PA = 7 cm, what is PB?"
        ),
        TextbookPassage(
            textbookName = "Class 10 Mathematics",
            educationLevel = "Class 10",
            subject = "Mathematics",
            chapterName = "Ch 13 & 14: Statistics and Probability",
            pageNumber = 260,
            content = "For grouped data: Mean = Σ(fixi)/Σfi. Mode = l + [(f1 - f0)/(2f1 - f0 - f2)] × h. Median = l + [(n/2 - cf)/f] × h. Empirical relation: 3 Median = Mode + 2 Mean. Probability of an event E: P(E) = (Number of favourable outcomes) / (Total number of possible outcomes), where 0 <= P(E) <= 1, and P(E) + P(not E) = 1.",
            realLifeExample = "Predicting the chance of getting heads on a coin toss (1/2) or calculating average marks of a Class 10 section.",
            formulaOrEquation = "3 Median = Mode + 2 Mean  |  P(E) + P(E') = 1",
            practiceQuestion = "A die is thrown once. What is the probability of getting a prime number?"
        ),

        // ==================== CLASS 10 SOCIAL SCIENCE ====================
        TextbookPassage(
            textbookName = "Class 10 Social Science",
            educationLevel = "Class 10",
            subject = "Social Science (History)",
            chapterName = "Nationalism in India & Europe",
            pageNumber = 30,
            content = "In Europe, the 1789 French Revolution spread ideas of liberty, equality, and nation-states, leading to the unification of Italy (Mazzini, Cavour, Garibaldi) and Germany (Otto von Bismarck). In India, Mahatma Gandhi led Satyagraha (Champaran, Kheda, Ahmedabad), the Non-Cooperation Movement (1920–22, withdrawn after Chauri Chaura), and the Civil Disobedience Movement (1930 Dandi Salt March). The 1929 Lahore Congress demanded Purna Swaraj (Complete Independence).",
            realLifeExample = "Gandhiji chose salt for the Dandi March because every household — rich or poor — used salt, uniting the entire nation.",
            formulaOrEquation = "1919 Rowlatt Act -> 1920 Non-Cooperation -> 1930 Dandi March -> 1942 Quit India",
            practiceQuestion = "Why did Mahatma Gandhi call off the Non-Cooperation Movement in 1922?"
        ),
        TextbookPassage(
            textbookName = "Class 10 Social Science",
            educationLevel = "Class 10",
            subject = "Social Science (Geography)",
            chapterName = "Resources, Agriculture & Water Conservation",
            pageNumber = 12,
            content = "Resources are classified as renewable/non-renewable and require sustainable development. Major Indian soils: Alluvial (most fertile, northern plains), Black/Regur (ideal for cotton, Deccan plateau), Red & Yellow, Laterite, Arid, and Forest soil. Indian cropping seasons: Kharif (monsoon: rice, maize, cotton), Rabi (winter: wheat, gram, mustard), Zaid (summer: watermelon, cucumber). Water conservation includes rainwater harvesting (tankas, Johads, bamboo drip irrigation) and multipurpose river projects.",
            realLifeExample = "Rooftop rainwater harvesting in rural homes recharges groundwater wells for summer months.",
            formulaOrEquation = "Kharif (June–Oct)  |  Rabi (Oct–March)  |  Zaid (March–June)",
            practiceQuestion = "Which soil type is best suited for growing cotton, and why?"
        ),
        TextbookPassage(
            textbookName = "Class 10 Social Science",
            educationLevel = "Class 10",
            subject = "Social Science (Civics & Economics)",
            chapterName = "Power Sharing, Federalism & Sectors of Economy",
            pageNumber = 22,
            content = "Democracy shares power horizontally (Legislature, Executive, Judiciary) and vertically (Union, State, Local Panchayati Raj — 73rd Amendment). Federalism divides powers via Union, State, and Concurrent Lists. In Economics, the three sectors are Primary (agriculture, dairy, fishing), Secondary (manufacturing, factories), and Tertiary (services: banking, transport, teaching). Formal credit (banks, cooperatives) offers fair interest rates compared to informal moneylenders. GDP measures total value of final goods and services produced in a year.",
            realLifeExample = "Self-Help Groups (SHGs) in villages help rural women pool savings and get low-interest bank loans without collateral.",
            formulaOrEquation = "Primary (Agriculture) -> Secondary (Industry) -> Tertiary (Services)",
            practiceQuestion = "Why are Self-Help Groups (SHGs) important for rural borrowers?"
        ),

        // ==================== CLASS 10 ENGLISH ====================
        TextbookPassage(
            textbookName = "Class 10 English",
            educationLevel = "Class 10",
            subject = "English",
            chapterName = "Grammar, Tenses & Letter Writing",
            pageNumber = 14,
            content = "Key Class 10 English Grammar: Tenses (Simple, Continuous, Perfect, Perfect Continuous in Present, Past, Future), Subject-Verb Agreement (singular subject takes singular verb), Active to Passive Voice (Object becomes Subject + be + V3), Reported Speech (backshift tense when reporting verb is in past: 'am' -> 'was', 'will' -> 'would'), Modals (can, could, may, might, must, should). Formal Letter format: Sender's Address, Date, Receiver's Address, Subject, Salutation, Body (3 paragraphs), Complimentary Close.",
            realLifeExample = "Direct: He said, 'I am studying.' -> Indirect: He said that he was studying. Active: The farmer sows seeds. -> Passive: Seeds are sown by the farmer.",
            formulaOrEquation = "Passive = Object + helping verb + V3 (Past Participle) + by + Subject",
            practiceQuestion = "Convert to Passive Voice: 'The teacher explained the lesson clearly.'"
        )
    )

    val initialQuizQuestions = listOf(
        QuizQuestionEntity(
            subject = "Science (Chemistry)",
            chapterName = "Ch 1: Chemical Reactions and Equations",
            difficulty = "Easy",
            question = "Why are potato chips packets flushed with nitrogen gas?",
            optionA = "To make them heavier",
            optionB = "To prevent oxidation (rancidity) of fats and oils",
            optionC = "To increase moisture",
            optionD = "To add salt flavour",
            correctIndex = 1,
            explanation = "Nitrogen is an inert antioxidant gas that prevents fats and oils in chips from oxidizing and becoming rancid."
        ),
        QuizQuestionEntity(
            subject = "Science (Chemistry)",
            chapterName = "Ch 2: Acids, Bases and Salts",
            difficulty = "Easy",
            question = "What is the chemical formula of Plaster of Paris?",
            optionA = "CaSO4·2H2O",
            optionB = "CaSO4·½H2O",
            optionC = "NaHCO3",
            optionD = "CaOCl2",
            correctIndex = 1,
            explanation = "Plaster of Paris is calcium sulphate hemihydrate (CaSO4·½H2O), obtained by heating gypsum at 373 K."
        ),
        QuizQuestionEntity(
            subject = "Science (Biology)",
            chapterName = "Ch 5: Life Processes",
            difficulty = "Easy",
            question = "Which gas is released as a by-product during photosynthesis?",
            optionA = "Carbon dioxide",
            optionB = "Nitrogen",
            optionC = "Oxygen",
            optionD = "Methane",
            correctIndex = 2,
            explanation = "During photosynthesis, water molecules are split using sunlight energy, releasing Oxygen (O2)."
        ),
        QuizQuestionEntity(
            subject = "Science (Physics)",
            chapterName = "Ch 11: Electricity",
            difficulty = "Easy",
            question = "If a resistor of 6 Ω carries a current of 2 A, what is the voltage across it?",
            optionA = "3 V",
            optionB = "8 V",
            optionC = "12 V",
            optionD = "0.33 V",
            correctIndex = 2,
            explanation = "By Ohm's Law, V = I × R = 2 A × 6 Ω = 12 V."
        ),
        QuizQuestionEntity(
            subject = "Science (Physics)",
            chapterName = "Ch 10: The Human Eye and the Colourful World",
            difficulty = "Medium",
            question = "Which lens is used to correct Myopia (near-sightedness)?",
            optionA = "Convex lens",
            optionB = "Concave lens",
            optionC = "Cylindrical lens",
            optionD = "Plane glass",
            correctIndex = 1,
            explanation = "A concave (diverging) lens of suitable power brings the image of distant objects back onto the retina."
        ),
        QuizQuestionEntity(
            subject = "Mathematics",
            chapterName = "Ch 3 & 4: Linear & Quadratic Equations",
            difficulty = "Medium",
            question = "When does ax² + bx + c = 0 have two equal real roots?",
            optionA = "b² - 4ac > 0",
            optionB = "b² - 4ac = 0",
            optionC = "b² - 4ac < 0",
            optionD = "a = 0",
            correctIndex = 1,
            explanation = "When discriminant D = b² - 4ac = 0, both roots are real and equal to -b/(2a)."
        ),
        QuizQuestionEntity(
            subject = "Mathematics",
            chapterName = "Ch 8 & 9: Trigonometry & Heights and Distances",
            difficulty = "Medium",
            question = "What is the value of sin² 30° + cos² 30°?",
            optionA = "0",
            optionB = "1/2",
            optionC = "1",
            optionD = "√3/2",
            correctIndex = 2,
            explanation = "By the fundamental trigonometric identity, sin²θ + cos²θ = 1 for any angle θ."
        ),
        QuizQuestionEntity(
            subject = "Social Science (History)",
            chapterName = "Nationalism in India & Europe",
            difficulty = "Easy",
            question = "Which movement began with Mahatma Gandhi's 1930 Dandi Salt March?",
            optionA = "Non-Cooperation Movement",
            optionB = "Civil Disobedience Movement",
            optionC = "Quit India Movement",
            optionD = "Khilafat Movement",
            correctIndex = 1,
            explanation = "Breaking the salt law at Dandi in April 1930 marked the beginning of the Civil Disobedience Movement."
        ),
        QuizQuestionEntity(
            subject = "Social Science (Geography)",
            chapterName = "Resources, Agriculture & Water Conservation",
            difficulty = "Medium",
            question = "Which soil is also known as Regur soil and is ideal for growing cotton?",
            optionA = "Alluvial soil",
            optionB = "Black soil",
            optionC = "Laterite soil",
            optionD = "Arid soil",
            correctIndex = 1,
            explanation = "Black (Regur) soil retains moisture well and is rich in calcium carbonate and potash, making it ideal for cotton."
        ),
        QuizQuestionEntity(
            subject = "English",
            chapterName = "Grammar, Tenses & Letter Writing",
            difficulty = "Easy",
            question = "Choose the correct Passive Voice for: 'The farmer sows seeds.'",
            optionA = "Seeds were sown by the farmer.",
            optionB = "Seeds are sown by the farmer.",
            optionC = "Seeds is sown by the farmer.",
            optionD = "Seeds are sowing by the farmer.",
            correctIndex = 1,
            explanation = "Simple Present Active ('sows') converts to 'are + V3 (sown)' in Passive Voice for plural object 'seeds'."
        )
    )

    val initialDictionary = listOf(
        DictionaryEntryEntity(
            word = "Photosynthesis",
            subject = "Science (Biology)",
            englishMeaning = "Process by which green plants convert sunlight, water, and CO2 into glucose and oxygen.",
            kannadaMeaning = "ದ್ಯುತಿಸಂಶ್ಲೇಷಣೆ (ಸೂರ್ಯನ ಬೆಳಕಿನಿಂದ ಸಸ್ಯಗಳು ಆಹಾರ ತಯಾರಿಸುವ ಕ್ರಿಯೆ)",
            hindiMeaning = "प्रकाश संश्लेषण (सूर्य के प्रकाश से पौधों द्वारा भोजन बनाना)",
            teluguMeaning = "కిరణజన్య సంయోగక్రియ",
            tamilMeaning = "ஒளிச்சேர்க்கை",
            exampleSentence = "Green leaves perform photosynthesis during daytime.",
            isFavorite = true
        ),
        DictionaryEntryEntity(
            word = "Catenation",
            subject = "Science (Chemistry)",
            englishMeaning = "The unique ability of carbon atoms to form strong covalent bonds with other carbon atoms to create long chains or rings.",
            kannadaMeaning = "ಸರಪಳಿ ರಚನೆ (ಕಾರ್ಬನ್ ಪರಮಾಣುಗಳು ಒಂದಕ್ಕೊಂದು ಸೇರಿ ಸರಪಳಿ ರಚಿಸುವ ಗುಣ)",
            hindiMeaning = "शृंखलन (कार्बन परमाणुओं का आपस में जुड़कर लंबी शृंखला बनाना)",
            teluguMeaning = "శృంఖల ధర్మం",
            tamilMeaning = "சங்கிலித்தொடராக்கம்",
            exampleSentence = "Carbon forms millions of compounds due to catenation and tetravalency."
        ),
        DictionaryEntryEntity(
            word = "Refraction",
            subject = "Science (Physics)",
            englishMeaning = "Bending of light when it travels obliquely from one transparent medium to another.",
            kannadaMeaning = "ವಕ್ರೀಭವನ (ಬೆಳಕು ಒಂದು ಮಾಧ್ಯಮದಿಂದ ಇನ್ನೊಂದಕ್ಕೆ ಹೋಗುವಾಗ ಬಾಗುವುದು)",
            hindiMeaning = "अपवर्तन (प्रकाश का एक माध्यम से दूसरे माध्यम में जाने पर मुड़ना)",
            teluguMeaning = "వక్రీభవనం",
            tamilMeaning = "ஒளிவிலகல்",
            exampleSentence = "A coin at the bottom of a water bucket looks raised due to refraction."
        ),
        DictionaryEntryEntity(
            word = "Discriminant",
            subject = "Mathematics",
            englishMeaning = "The expression D = b² - 4ac in a quadratic equation that determines the nature of its roots.",
            kannadaMeaning = "ಶೋಧಕ (ವರ್ಗ ಸಮೀಕರಣದ ಮೂಲಗಳ ಸ್ವಭಾವ ತಿಳಿಸುವ ಸೂತ್ರ)",
            hindiMeaning = "विविक्तकर (द्विघात समीकरण के मूलों की प्रकृति बताने वाला मान)",
            teluguMeaning = "విచక్షణి",
            tamilMeaning = "தன்மைக்காட்டி",
            exampleSentence = "If the discriminant is zero, the quadratic equation has two equal real roots."
        ),
        DictionaryEntryEntity(
            word = "Federalism",
            subject = "Social Science",
            englishMeaning = "A system of government in which power is divided between a central authority and constituent state/local units.",
            kannadaMeaning = "ಒಕ್ಕೂಟ ವ್ಯವಸ್ಥೆ (ಕೇಂದ್ರ ಮತ್ತು ರಾಜ್ಯ ಸರ್ಕಾರಗಳ ನಡುವೆ ಅಧಿಕಾರ ಹಂಚಿಕೆ)",
            hindiMeaning = "संघवाद (केंद्र और राज्य सरकारों के बीच शक्ति का विभाजन)",
            teluguMeaning = "సమాఖ్య విధానం",
            tamilMeaning = "கூட்டாட்சி",
            exampleSentence = "India practices federalism with Union, State, and Panchayati Raj tiers."
        )
    )

    val initialStudyPlans = listOf(
        StudyPlanEntity(
            title = "Revise Ch 11: Electricity & Ohm's Law",
            subject = "Science (Physics)",
            targetDate = "Today, 6:00 PM",
            isExam = false,
            isCompleted = false
        ),
        StudyPlanEntity(
            title = "Practice Trigonometry & AP Formulas",
            subject = "Mathematics",
            targetDate = "Tomorrow, 7:00 AM",
            isExam = false,
            isCompleted = false
        ),
        StudyPlanEntity(
            title = "Class 10 Board Pre-Final Exam",
            subject = "All Subjects",
            targetDate = "In 14 days",
            isExam = true,
            isCompleted = false
        )
    )

    val initialFlashcards = listOf(
        FlashcardEntity(
            subject = "Science (Chemistry)",
            chapterName = "Ch 2: Acids, Bases and Salts",
            front = "What is the formula of Baking Soda & Washing Soda?",
            back = "Baking Soda: NaHCO3 (Sodium hydrogencarbonate) | Washing Soda: Na2CO3·10H2O"
        ),
        FlashcardEntity(
            subject = "Science (Physics)",
            chapterName = "Ch 9: Light",
            front = "Write the Mirror Formula and Lens Formula",
            back = "Mirror: 1/v + 1/u = 1/f  |  Lens: 1/v - 1/u = 1/f"
        ),
        FlashcardEntity(
            subject = "Science (Biology)",
            chapterName = "Ch 8: Heredity",
            front = "What are Mendel's Monohybrid and Dihybrid F2 phenotypic ratios?",
            back = "Monohybrid F2 = 3:1  |  Dihybrid F2 = 9:3:3:1"
        ),
        FlashcardEntity(
            subject = "Mathematics",
            chapterName = "Ch 5: Arithmetic Progressions",
            front = "Write the nth term and Sum of n terms of an AP",
            back = "an = a + (n - 1)d  |  Sn = (n/2)[2a + (n - 1)d]"
        )
    )

    val initialFormulas = listOf(
        FormulaEntity(subject = "Science (Physics)", title = "Ohm's Law & Power", formula = "V = IR  |  P = VI = I²R", variablesNote = "V: Volt, I: Ampere, R: Ohm, P: Watt"),
        FormulaEntity(subject = "Science (Physics)", title = "Mirror & Lens Formula", formula = "1/v + 1/u = 1/f (Mirror)  |  1/v - 1/u = 1/f (Lens)", variablesNote = "u: object dist, v: image dist, f: focal length"),
        FormulaEntity(subject = "Science (Chemistry)", title = "Photosynthesis", formula = "6CO₂ + 6H₂O → C₆H₁₂O₆ + 6O₂", variablesNote = "In presence of sunlight & chlorophyll"),
        FormulaEntity(subject = "Mathematics", title = "Quadratic Formula", formula = "x = (-b ± √(b² - 4ac)) / 2a", variablesNote = "Discriminant D = b² - 4ac"),
        FormulaEntity(subject = "Mathematics", title = "Arithmetic Progression", formula = "an = a + (n-1)d  |  Sn = (n/2)[2a + (n-1)d]", variablesNote = "a: first term, d: common difference"),
        FormulaEntity(subject = "Mathematics", title = "Trigonometric Identities", formula = "sin²θ + cos²θ = 1  |  1 + tan²θ = sec²θ", variablesNote = "Also 1 + cot²θ = cosec²θ"),
        FormulaEntity(subject = "Mathematics", title = "Statistics Empirical Rule", formula = "3 Median = Mode + 2 Mean", variablesNote = "Relates central tendency measures")
    )
}
