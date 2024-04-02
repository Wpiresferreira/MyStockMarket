package com.example.myapplication;


import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;


public class Controller extends AppCompatActivity {

    private static Customer loggedUser;
    static String token = "cmo6he1r01qj3mal97u0cmo6he1r01qj3mal97ug";
    static String lastTransactionSymbol;

    static List<String> companies = Arrays.asList("A AGILENT TECHNOLOGIES INC",
            "AAL AMERICAN AIRLINES GROUP INC",
            "AAPL APPLE INC",
            "ABBV ABBVIE INC",
            "ABEV AMBEV SA-ADR",
            "ABNB AIRBNB INC-CLASS A",
            "ABT ABBOTT LABORATORIES",
            "ACGL ARCH CAPITAL GROUP LTD",
            "ACN ACCENTURE PLC-CL A",
            "ADBE ADOBE INC",
            "ADI ANALOG DEVICES INC",
            "ADM ARCHER-DANIELS-MIDLAND CO",
            "ADP AUTOMATIC DATA PROCESSING",
            "ADSK AUTODESK INC",
            "AEE AMEREN CORPORATION",
            "AEP AMERICAN ELECTRIC POWER",
            "AES AES CORP",
            "AFL AFLAC INC",
            "AIG AMERICAN INTERNATIONAL GROUP",
            "AIZ ASSURANT INC",
            "AJG ARTHUR J GALLAGHER & CO",
            "AKAM AKAMAI TECHNOLOGIES INC",
            "ALB ALBEMARLE CORP",
            "ALGN ALIGN TECHNOLOGY INC",
            "ALL ALLSTATE CORP",
            "ALLE ALLEGION PLC",
            "AMAT APPLIED MATERIALS INC",
            "AMCR AMCOR PLC",
            "AMD ADVANCED MICRO DEVICES",
            "AME AMETEK INC",
            "AMGN AMGEN INC",
            "AMP AMERIPRISE FINANCIAL INC",
            "AMT AMERICAN TOWER CORP",
            "AMZN AMAZON.COM INC",
            "ANET ARISTA NETWORKS INC",
            "ANSS ANSYS INC",
            "AON AON PLC-CLASS A",
            "AOS SMITH (A.O.) CORP",
            "APA APA CORP",
            "APD AIR PRODUCTS & CHEMICALS INC",
            "APH AMPHENOL CORP-CL A",
            "APTV APTIV PLC",
            "ARE ALEXANDRIA REAL ESTATE EQUIT",
            "ATO ATMOS ENERGY CORP",
            "AVB AVALONBAY COMMUNITIES INC",
            "AVGO BROADCOM INC",
            "AVY AVERY DENNISON CORP",
            "AWK AMERICAN WATER WORKS CO INC",
            "AXON AXON ENTERPRISE INC",
            "AXP AMERICAN EXPRESS CO",
            "AZO AUTOZONE INC",
            "BA BOEING CO/THE",
            "BAC BANK OF AMERICA CORP",
            "BALL BALL CORP",
            "BAX BAXTER INTERNATIONAL INC",
            "BBWI BATH & BODY WORKS INC",
            "BBY BEST BUY CO INC",
            "BDORY BANCO DO BRASIL SA-SPON ADR",
            "BDX BECTON DICKINSON AND CO",
            "BEN FRANKLIN RESOURCES INC",
            "BF.B BROWN-FORMAN CORP-CLASS B",
            "BG BUNGE GLOBAL SA",
            "BIIB BIOGEN INC",
            "BIO BIO-RAD LABORATORIES-A",
            "BK BANK OF NEW YORK MELLON CORP",
            "BKNG BOOKING HOLDINGS INC",
            "BKR BAKER HUGHES CO",
            "BLDR BUILDERS FIRSTSOURCE INC",
            "BLK BLACKROCK INC",
            "BMY BRISTOL-MYERS SQUIBB CO",
            "BR BROADRIDGE FINANCIAL SOLUTIO",
            "BRK.B BERKSHIRE HATHAWAY INC-CL B",
            "BRO BROWN & BROWN INC",
            "BSX BOSTON SCIENTIFIC CORP",
            "BTC-USD BITCOIN USD",
            "BWA BORGWARNER INC",
            "BX BLACKSTONE INC",
            "BXP BOSTON PROPERTIES INC",
            "C CITIGROUP INC",
            "CAG CONAGRA BRANDS INC",
            "CAH CARDINAL HEALTH INC",
            "CARR CARRIER GLOBAL CORP",
            "CAT CATERPILLAR INC",
            "CB CHUBB LTD",
            "CBOE CBOE GLOBAL MARKETS INC",
            "CBRE CBRE GROUP INC - A",
            "CCI CROWN CASTLE INC",
            "CCL CARNIVAL CORP",
            "CDNS CADENCE DESIGN SYS INC",
            "CDW CDW CORP/DE",
            "CE CELANESE CORP",
            "CEG CONSTELLATION ENERGY",
            "CF CF INDUSTRIES HOLDINGS INC",
            "CFG CITIZENS FINANCIAL GROUP",
            "CHD CHURCH & DWIGHT CO INC",
            "CHRW C.H. ROBINSON WORLDWIDE INC",
            "CHTR CHARTER COMMUNICATIONS INC-A",
            "CI THE CIGNA GROUP",
            "CINF CINCINNATI FINANCIAL CORP",
            "CL COLGATE-PALMOLIVE CO",
            "CLX CLOROX COMPANY",
            "CMA COMERICA INC",
            "CMCSA COMCAST CORP-CLASS A",
            "CME CME GROUP INC",
            "CMG CHIPOTLE MEXICAN GRILL INC",
            "CMI CUMMINS INC",
            "CMS CMS ENERGY CORP",
            "CNC CENTENE CORP",
            "CNP CENTERPOINT ENERGY INC",
            "COF CAPITAL ONE FINANCIAL CORP",
            "COO COOPER COS INC/THE",
            "COP CONOCOPHILLIPS",
            "COR CENCORA INC",
            "COST COSTCO WHOLESALE CORP",
            "CPB CAMPBELL SOUP CO",
            "CPRT COPART INC",
            "CPT CAMDEN PROPERTY TRUST",
            "CRL CHARLES RIVER LABORATORIES",
            "CRM SALESFORCE INC",
            "CSCO CISCO SYSTEMS INC",
            "CSGP COSTAR GROUP INC",
            "CSX CSX CORP",
            "CTAS CINTAS CORP",
            "CTLT CATALENT INC",
            "CTRA COTERRA ENERGY INC",
            "CTSH COGNIZANT TECH SOLUTIONS-A",
            "CTVA CORTEVA INC",
            "CVS CVS HEALTH CORP",
            "CVX CHEVRON CORP",
            "CZR CAESARS ENTERTAINMENT INC",
            "D DOMINION ENERGY INC",
            "DAL DELTA AIR LINES INC",
            "DAY DAYFORCE INC",
            "DD DUPONT DE NEMOURS INC",
            "DE DEERE & CO",
            "DFS DISCOVER FINANCIAL SERVICES",
            "DG DOLLAR GENERAL CORP",
            "DGX QUEST DIAGNOSTICS INC",
            "DHI DR HORTON INC",
            "DHR DANAHER CORP",
            "DIS WALT DISNEY CO/THE",
            "DLR DIGITAL REALTY TRUST INC",
            "DLTR DOLLAR TREE INC",
            "DOV DOVER CORP",
            "DOW DOW INC",
            "DPZ DOMINO'S PIZZA INC",
            "DRI DARDEN RESTAURANTS INC",
            "DTE DTE ENERGY COMPANY",
            "DUK DUKE ENERGY CORP",
            "DVA DAVITA INC",
            "DVN DEVON ENERGY CORP",
            "DXCM DEXCOM INC",
            "EA ELECTRONIC ARTS INC",
            "EBAY EBAY INC",
            "ECL ECOLAB INC",
            "ED CONSOLIDATED EDISON INC",
            "EFX EQUIFAX INC",
            "EG EVEREST GROUP LTD",
            "EIX EDISON INTERNATIONAL",
            "EL ESTEE LAUDER COMPANIES-CL A",
            "ELV ELEVANCE HEALTH INC",
            "EMN EASTMAN CHEMICAL CO",
            "EMR EMERSON ELECTRIC CO",
            "ENPH ENPHASE ENERGY INC",
            "EOG EOG RESOURCES INC",
            "EPAM EPAM SYSTEMS INC",
            "EQIX EQUINIX INC",
            "EQR EQUITY RESIDENTIAL",
            "EQT EQT CORP",
            "ES EVERSOURCE ENERGY",
            "ESS ESSEX PROPERTY TRUST INC",
            "ETN EATON CORP PLC",
            "ETR ENTERGY CORP",
            "ETSY ETSY INC",
            "EVRG EVERGY INC",
            "EW EDWARDS LIFESCIENCES CORP",
            "EXC EXELON CORP",
            "EXPD EXPEDITORS INTL WASH INC",
            "EXPE EXPEDIA GROUP INC",
            "EXR EXTRA SPACE STORAGE INC",
            "F FORD MOTOR CO",
            "FANG DIAMONDBACK ENERGY INC",
            "FAST FASTENAL CO",
            "FCX FREEPORT-MCMORAN INC",
            "FDS FACTSET RESEARCH SYSTEMS INC",
            "FDX FEDEX CORP",
            "FE FIRSTENERGY CORP",
            "FFIV F5 INC",
            "FI FISERV INC",
            "FICO FAIR ISAAC CORP",
            "FIS FIDELITY NATIONAL INFO SERV",
            "FITB FIFTH THIRD BANCORP",
            "FMC FMC CORP",
            "FOX FOX CORP - CLASS B",
            "FOXA FOX CORP - CLASS A",
            "FRT FEDERAL REALTY INVS TRUST",
            "FSLR FIRST SOLAR INC",
            "FTNT FORTINET INC",
            "FTV FORTIVE CORP",
            "GD GENERAL DYNAMICS CORP",
            "GE GENERAL ELECTRIC CO",
            "GEHC GE HEALTHCARE TECHNOLOGY",
            "GEN GEN DIGITAL INC",
            "GILD GILEAD SCIENCES INC",
            "GIS GENERAL MILLS INC",
            "GL GLOBE LIFE INC",
            "GLW CORNING INC",
            "GM GENERAL MOTORS CO",
            "GNRC GENERAC HOLDINGS INC",
            "GOOG ALPHABET INC-CL C",
            "GOOGL ALPHABET INC-CL A",
            "GPC GENUINE PARTS CO",
            "GPN GLOBAL PAYMENTS INC",
            "GRMN GARMIN LTD",
            "GS GOLDMAN SACHS GROUP INC",
            "GWW WW GRAINGER INC",
            "HAL HALLIBURTON CO",
            "HAS HASBRO INC",
            "HBAN HUNTINGTON BANCSHARES INC",
            "HCA HCA HEALTHCARE INC",
            "HD HOME DEPOT INC",
            "HES HESS CORP",
            "HIG HARTFORD FINANCIAL SVCS GRP",
            "HII HUNTINGTON INGALLS INDUSTRIE",
            "HLT HILTON WORLDWIDE HOLDINGS IN",
            "HOLX HOLOGIC INC",
            "HON HONEYWELL INTERNATIONAL INC",
            "HPE HEWLETT PACKARD ENTERPRISE",
            "HPQ HP INC",
            "HRL HORMEL FOODS CORP",
            "HSIC HENRY SCHEIN INC",
            "HST HOST HOTELS & RESORTS INC",
            "HSY HERSHEY CO/THE",
            "HUBB HUBBELL INC",
            "HUM HUMANA INC",
            "HWM HOWMET AEROSPACE INC",
            "IBM INTL BUSINESS MACHINES CORP",
            "ICE INTERCONTINENTAL EXCHANGE IN",
            "IDXX IDEXX LABORATORIES INC",
            "IEX IDEX CORP",
            "IFF INTL FLAVORS & FRAGRANCES",
            "ILMN ILLUMINA INC",
            "INCY INCYTE CORP",
            "INTC INTEL CORP",
            "INTU INTUIT INC",
            "INVH INVITATION HOMES INC",
            "IP INTERNATIONAL PAPER CO",
            "IPG INTERPUBLIC GROUP OF COS INC",
            "IQV IQVIA HOLDINGS INC",
            "IR INGERSOLL-RAND INC",
            "IRM IRON MOUNTAIN INC",
            "ISRG INTUITIVE SURGICAL INC",
            "IT GARTNER INC",
            "ITW ILLINOIS TOOL WORKS",
            "IVZ INVESCO LTD",
            "J JACOBS SOLUTIONS INC",
            "JBHT HUNT (JB) TRANSPRT SVCS INC",
            "JBL JABIL INC",
            "JCI JOHNSON CONTROLS INTERNATION",
            "JKHY JACK HENRY & ASSOCIATES INC",
            "JNJ JOHNSON & JOHNSON",
            "JNPR JUNIPER NETWORKS INC",
            "JPM JPMORGAN CHASE & CO",
            "K KELLANOVA",
            "KDP KEURIG DR PEPPER INC",
            "KEY KEYCORP",
            "KEYS KEYSIGHT TECHNOLOGIES IN",
            "KHC KRAFT HEINZ CO/THE",
            "KIM KIMCO REALTY CORP",
            "KLAC KLA CORP",
            "KMB KIMBERLY-CLARK CORP",
            "KMI KINDER MORGAN INC",
            "KMX CARMAX INC",
            "KO COCA-COLA CO/THE",
            "KR KROGER CO",
            "KVUE KENVUE INC",
            "L LOEWS CORP",
            "LDOS LEIDOS HOLDINGS INC",
            "LEN LENNAR CORP-A",
            "LH LABORATORY CRP OF AMER HLDGS",
            "LHX L3HARRIS TECHNOLOGIES INC",
            "LIN LINDE PLC",
            "LKQ LKQ CORP",
            "LLY ELI LILLY & CO",
            "LMT LOCKHEED MARTIN CORP",
            "LNT ALLIANT ENERGY CORP",
            "LOW LOWE'S COS INC",
            "LRCX LAM RESEARCH CORP",
            "LULU LULULEMON ATHLETICA INC",
            "LUV SOUTHWEST AIRLINES CO",
            "LVS LAS VEGAS SANDS CORP",
            "LW LAMB WESTON HOLDINGS INC",
            "LYB LYONDELLBASELL INDU-CL A",
            "LYV LIVE NATION ENTERTAINMENT IN",
            "MA MASTERCARD INC - A",
            "MAA MID-AMERICA APARTMENT COMM",
            "MAR MARRIOTT INTERNATIONAL -CL A",
            "MAS MASCO CORP",
            "MCD MCDONALD'S CORP",
            "MCHP MICROCHIP TECHNOLOGY INC",
            "MCK MCKESSON CORP",
            "MCO MOODY'S CORP",
            "MDLZ MONDELEZ INTERNATIONAL INC-A",
            "MDT MEDTRONIC PLC",
            "MET METLIFE INC",
            "META META PLATFORMS INC-CLASS A",
            "MGM MGM RESORTS INTERNATIONAL",
            "MHK MOHAWK INDUSTRIES INC",
            "MKC MCCORMICK & CO-NON VTG SHRS",
            "MKTX MARKETAXESS HOLDINGS INC",
            "MLM MARTIN MARIETTA MATERIALS",
            "MMC MARSH & MCLENNAN COS",
            "MMM 3M CO",
            "MNST MONSTER BEVERAGE CORP",
            "MO ALTRIA GROUP INC",
            "MOH MOLINA HEALTHCARE INC",
            "MOS MOSAIC CO/THE",
            "MPC MARATHON PETROLEUM CORP",
            "MPWR MONOLITHIC POWER SYSTEMS INC",
            "MRK MERCK & CO. INC.",
            "MRNA MODERNA INC",
            "MRO MARATHON OIL CORP",
            "MS MORGAN STANLEY",
            "MSCI MSCI INC",
            "MSFT MICROSOFT CORP",
            "MSI MOTOROLA SOLUTIONS INC",
            "MTB M & T BANK CORP",
            "MTCH MATCH GROUP INC",
            "MTD METTLER-TOLEDO INTERNATIONAL",
            "MU MICRON TECHNOLOGY INC",
            "NCLH NORWEGIAN CRUISE LINE HOLDIN",
            "NDAQ NASDAQ INC",
            "NDSN NORDSON CORP",
            "NEE NEXTERA ENERGY INC",
            "NEM NEWMONT CORP",
            "NFLX NETFLIX INC",
            "NI NISOURCE INC",
            "NKE NIKE INC -CL B",
            "NOC NORTHROP GRUMMAN CORP",
            "NOW SERVICENOW INC",
            "NRG NRG ENERGY INC",
            "NSC NORFOLK SOUTHERN CORP",
            "NTAP NETAPP INC",
            "NTRS NORTHERN TRUST CORP",
            "NUE NUCOR CORP",
            "NVDA NVIDIA CORP",
            "NVR NVR INC",
            "NWS NEWS CORP - CLASS B",
            "NWSA NEWS CORP - CLASS A",
            "NXPI NXP SEMICONDUCTORS NV",
            "O REALTY INCOME CORP",
            "ODFL OLD DOMINION FREIGHT LINE",
            "OKE ONEOK INC",
            "OMC OMNICOM GROUP",
            "ON ON SEMICONDUCTOR",
            "ORCL ORACLE CORP",
            "ORLY O'REILLY AUTOMOTIVE INC",
            "OTIS OTIS WORLDWIDE CORP",
            "OXY OCCIDENTAL PETROLEUM CORP",
            "PANW PALO ALTO NETWORKS INC",
            "PARA PARAMOUNT GLOBAL-CLASS B",
            "PAYC PAYCOM SOFTWARE INC",
            "PAYX PAYCHEX INC",
            "PBR PETROLEO BRASILEIRO-SPON ADR",
            "PCAR PACCAR INC",
            "PCG P G & E CORP",
            "PEG PUBLIC SERVICE ENTERPRISE GP",
            "PEP PEPSICO INC",
            "PFE PFIZER INC",
            "PFG PRINCIPAL FINANCIAL GROUP",
            "PG PROCTER & GAMBLE CO/THE",
            "PGR PROGRESSIVE CORP",
            "PH PARKER HANNIFIN CORP",
            "PHM PULTEGROUP INC",
            "PKG PACKAGING CORP OF AMERICA",
            "PLD PROLOGIS INC",
            "PM PHILIP MORRIS INTERNATIONAL",
            "PNC PNC FINANCIAL SERVICES GROUP",
            "PNR PENTAIR PLC",
            "PNW PINNACLE WEST CAPITAL",
            "PODD INSULET CORP",
            "POOL POOL CORP",
            "PPG PPG INDUSTRIES INC",
            "PPL PPL CORP",
            "PRU PRUDENTIAL FINANCIAL INC",
            "PSA PUBLIC STORAGE",
            "PSX PHILLIPS 66",
            "PTC PTC INC",
            "PWR QUANTA SERVICES INC",
            "PXD PIONEER NATURAL RESOURCES CO",
            "PYPL PAYPAL HOLDINGS INC",
            "QCOM QUALCOMM INC",
            "QRVO QORVO INC",
            "RCL ROYAL CARIBBEAN CRUISES LTD",
            "REG REGENCY CENTERS CORP",
            "REGN REGENERON PHARMACEUTICALS",
            "RF REGIONS FINANCIAL CORP",
            "RHI ROBERT HALF INC",
            "RJF RAYMOND JAMES FINANCIAL INC",
            "RL RALPH LAUREN CORP",
            "RMD RESMED INC",
            "ROK ROCKWELL AUTOMATION INC",
            "ROL ROLLINS INC",
            "ROP ROPER TECHNOLOGIES INC",
            "ROST ROSS STORES INC",
            "RSG REPUBLIC SERVICES INC",
            "RTX RTX CORP",
            "RVTY REVVITY INC",
            "SBAC SBA COMMUNICATIONS CORP",
            "SBUX STARBUCKS CORP",
            "SCHW SCHWAB (CHARLES) CORP",
            "SHW SHERWIN-WILLIAMS CO/THE",
            "SJM JM SMUCKER CO/THE",
            "SLB SCHLUMBERGER LTD",
            "SNA SNAP-ON INC",
            "SNPS SYNOPSYS INC",
            "SO SOUTHERN CO/THE",
            "SPG SIMON PROPERTY GROUP INC",
            "SPGI S&P GLOBAL INC",
            "SRE SEMPRA",
            "STE STERIS PLC",
            "STLD STEEL DYNAMICS INC",
            "STNE STONECO LTD-A",
            "STT STATE STREET CORP",
            "STX SEAGATE TECHNOLOGY HOLDINGS",
            "STZ CONSTELLATION BRANDS INC-A",
            "SWK STANLEY BLACK & DECKER INC",
            "SWKS SKYWORKS SOLUTIONS INC",
            "SYF SYNCHRONY FINANCIAL",
            "SYK STRYKER CORP",
            "SYY SYSCO CORP",
            "T AT&T INC",
            "TAP MOLSON COORS BEVERAGE CO - B",
            "TDG TRANSDIGM GROUP INC",
            "TDY TELEDYNE TECHNOLOGIES INC",
            "TECH BIO-TECHNE CORP",
            "TEL TE CONNECTIVITY LTD",
            "TER TERADYNE INC",
            "TFC TRUIST FINANCIAL CORP",
            "TFX TELEFLEX INC",
            "TGT TARGET CORP",
            "TJX TJX COMPANIES INC",
            "TMO THERMO FISHER SCIENTIFIC INC",
            "TMUS T-MOBILE US INC",
            "TPR TAPESTRY INC",
            "TRGP TARGA RESOURCES CORP",
            "TRMB TRIMBLE INC",
            "TROW T ROWE PRICE GROUP INC",
            "TRV TRAVELERS COS INC/THE",
            "TSCO TRACTOR SUPPLY COMPANY",
            "TSLA TESLA INC",
            "TSN TYSON FOODS INC-CL A",
            "TT TRANE TECHNOLOGIES PLC",
            "TTWO TAKE-TWO INTERACTIVE SOFTWRE",
            "TXN TEXAS INSTRUMENTS INC",
            "TXT TEXTRON INC",
            "TYL TYLER TECHNOLOGIES INC",
            "UAL UNITED AIRLINES HOLDINGS INC",
            "UBER UBER TECHNOLOGIES INC",
            "UDR UDR INC",
            "UHS UNIVERSAL HEALTH SERVICES-B",
            "ULTA ULTA BEAUTY INC",
            "UNH UNITEDHEALTH GROUP INC",
            "UNP UNION PACIFIC CORP",
            "UPS UNITED PARCEL SERVICE-CL B",
            "URI UNITED RENTALS INC",
            "USB US BANCORP",
            "V VISA INC-CLASS A SHARES",
            "VALE VALE SA-SP ADR",
            "VFC VF CORP",
            "VICI VICI PROPERTIES INC",
            "VLO VALERO ENERGY CORP",
            "VLTO VERALTO CORP",
            "VMC VULCAN MATERIALS CO",
            "VRSK VERISK ANALYTICS INC",
            "VRSN VERISIGN INC",
            "VRTX VERTEX PHARMACEUTICALS INC",
            "VTR VENTAS INC",
            "VTRS VIATRIS INC",
            "VZ VERIZON COMMUNICATIONS INC",
            "WAB WABTEC CORP",
            "WAT WATERS CORP",
            "WBA WALGREENS BOOTS ALLIANCE INC",
            "WBD WARNER BROS DISCOVERY INC",
            "WDC WESTERN DIGITAL CORP",
            "WEC WEC ENERGY GROUP INC",
            "WELL WELLTOWER INC",
            "WFC WELLS FARGO & CO",
            "WHR WHIRLPOOL CORP",
            "WM WASTE MANAGEMENT INC",
            "WMB WILLIAMS COS INC",
            "WMT WALMART INC",
            "WRB WR BERKLEY CORP",
            "WRK WESTROCK CO",
            "WST WEST PHARMACEUTICAL SERVICES",
            "WTW WILLIS TOWERS WATSON PLC",
            "WY WEYERHAEUSER CO",
            "WYNN WYNN RESORTS LTD",
            "XEL XCEL ENERGY INC",
            "XOM EXXON MOBIL CORP",
            "XRAY DENTSPLY SIRONA INC",
            "XYL XYLEM INC",
            "YUM YUM! BRANDS INC",
            "ZBH ZIMMER BIOMET HOLDINGS INC",
            "ZBRA ZEBRA TECHNOLOGIES CORP-CL A",
            "ZION ZIONS BANCORP NA",
            "ZTS ZOETIS INC"
    );


    private Controller() {

    }

    public static Customer getLoggedUser() {
        return loggedUser;
    }

//    public static void getQuote(StockQuote stockQuote, Context context) {
//
//        String url = "https://finnhub.io/api/v1/quote?symbol=" +
//                stockQuote.symbol +
//                "&token=cmo6he1r01qj3mal97u0cmo6he1r01qj3mal97ug";
//
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    stockQuote.currentPrice = (double) response.getDouble("c");
//                    stockQuote.change = (double) response.getDouble("d");
//                    stockQuote.percentChange = (double) response.getDouble("dp");
//                    stockQuote.highPriceOfTheDay = (double) response.getDouble("h");
//                    stockQuote.lowPriceOfTheDay = (double) response.getDouble("l");
//                    stockQuote.openPriceOfTheDay = (double) response.getDouble("o");
//                    stockQuote.previousClosePrice = (double) response.getDouble("pc");
//
//                    TransactionActivity.textView_CurrentPrice.setText(numberFormatCurrency.format(stockQuote.currentPrice));
//                    TransactionActivity.textView_Change.setText(numberFormatCurrency.format(stockQuote.change));
//                    if (stockQuote.change > 0) {
//                        TransactionActivity.textView_Change.setTextColor(Color.GREEN);
//                    } else if (stockQuote.change < 0) {
//                        TransactionActivity.textView_Change.setTextColor(Color.RED);
//                    } else {
//                        TransactionActivity.textView_Change.setTextColor(Color.BLACK);
//                    }
//
//
//                    TransactionActivity.textView_PercentChange.setText(numberFormatInstance.format(stockQuote.percentChange));
//                    if (stockQuote.percentChange > 0) {
//                        TransactionActivity.textView_PercentChange.setTextColor(Color.GREEN);
//                    } else if (stockQuote.change < 0) {
//                        TransactionActivity.textView_PercentChange.setTextColor(Color.RED);
//                    } else {
//                        TransactionActivity.textView_PercentChange.setTextColor(Color.BLACK);
//                    }
//                    TransactionActivity.textView_Low.setText(numberFormatCurrency.format(stockQuote.lowPriceOfTheDay));
//                    TransactionActivity.textView_High.setText(numberFormatCurrency.format(stockQuote.highPriceOfTheDay));
//                    TransactionActivity.textView_Open.setText(numberFormatCurrency.format(stockQuote.openPriceOfTheDay));
//                    TransactionActivity.textView_PreviousClose.setText(numberFormatCurrency.format(stockQuote.previousClosePrice));
//                    int qt = Integer.parseInt(TransactionActivity.editText_Qt.getText().toString());
//                    TransactionActivity.textView_Total.setText(String.valueOf(stockQuote.currentPrice * qt));
//
//                } catch (JSONException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        Volley.newRequestQueue(MyApplication.getAppContext()).add(request);
//
//
//    }


//    public static void getName(StockQuote stockQuote, Context context) {
//
//        String url = "https://finnhub.io/api/v1/stock/profile2?symbol=" +
//                stockQuote.symbol +
//                "&token=cmo6he1r01qj3mal97u0cmo6he1r01qj3mal97ug";
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    stockQuote.name = response.getString("name");
//                    stockQuote.imageURL = response.getString("logo");
//                    TransactionActivity.textView_Name.setText(stockQuote.name);
//
//                } catch (JSONException e) {
////                    throw new RuntimeException(e);
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        Volley.newRequestQueue(context).add(request);
//    }


    public static boolean isValidUsername(String username) {

        // Check if username size is less than 8
        if (username.length() < 8) {
            return false;
        }
        // Check if username contains "@"
        if (!username.substring(2).contains("@")) {
            return false;
        }
        // Check if username contains .xx or .xxx
        return username.charAt(username.length() - 4) == '.' || username.charAt(username.length() - 3) == '.';
    }

    public static void updateLoggedUser(@NonNull Context context) {

        SharedPreferences sharedPref;
        sharedPref = context.getSharedPreferences(
                "com.example.myapplication.users", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Controller.loggedUser.username, new Gson().toJson(Controller.loggedUser));
        editor.apply();
    }

    public static void disconnectLoggedUser() {
        loggedUser = null;
    }

    public static void updatePassword(Context context, String password) {
        loggedUser.password = password;
        updateLoggedUser(context);
    }

    public static void addStockInWatchList(Context context, StockQuote stockQuote) {
        loggedUser.stocksInWatchlist.add(stockQuote);
        updateLoggedUser(context);
    }

    public static void logUser(Customer loadedCustomerObj) {
        loggedUser = loadedCustomerObj;
    }


}