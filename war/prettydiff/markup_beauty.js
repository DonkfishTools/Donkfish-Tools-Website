/*
This code may be used internally to Travelocity without limitation,
exclusion, or restriction.  If this code is used externally the
following comment must be included everywhere this code is used.
*/

/***********************************************************************
This is written by Austin Cheney on 7 May 2009.  Anybody may use this
code without permission so long as this comment exists verbatim in each
instance of its use.

http://www.travelocity.com/
http://mailmarkup.org/
***********************************************************************/

/*
This application serves to beautify markup languages.  The intent of
this application is to be language independant so long as the language
is tag based using "<" as the tag opening delimiter and ">" as the tag
ending delimiter.  The input does not have to be well formed or valid by
any means so long as it can be determined where a tag begins and where a
tag ends.  Additionally, the code also supports nested tags, such as
JSTL tags <c:out value="<em>text</em>"/>.

The only HTML specific area inferred by this application is that the
contents of "script" tags are presumed to be JavaScript and the contents
of "style" tags are presumed to be CSS.

Since this code is entirely language independant it does not make
assumptions on vocabulary.  This means singleton tags, tags that occur
in a singular use opposed to tags that exist as a pair with one to serve
as an opening and the other to serve as the closing tag, are only
identified if they end with "/>".  Otherwise singleton tags are presumed
to be opening tags will indent following tags as such.

This code was created for three reasons:
1) Create an application that is more open and friendly to customization

2) Provide an application that recognizes many various tag grammars
   The default recognized tag types are:
     * SGML comments "<!-- x -->"
     * SSI tags "<!--#command -->"
     * ASP tags "<% x %>"
     * PHP tags "<?php x ?>" - Please note that php tags must begin with
          "<?php" and not "<?" to prevent collision with XML parsing
          statements.  No support is provided for tags that start with
          only "<?", and so they are considered to be just another start
          tag.
     * XML parsing commands "<?xml x ?>"
     * SGML parsing commands "<!command x>"
     * start tags "<div>"
     * closing tags "</div>"
     * singleton tags "<link/>" or "<link />"

   !! It should be noted that at this time the contents of ASP and PHP
   tags are completely preserved.  The only impact is that the opening
   line of these tags is indented exactly like a singleton tag up to the
   first line break.

3) This provides beautification that does not insert extra whitespace
   into the default whitespace parsed, tokenized, output.  This is
   accomplished by defining content as four different types:
     * content - This type does not begin or end with whitespace
     * mixed_start - This type begins but not ends with whitespace
     * mixed_end - This type ends but not begins with whitespace
     * mixed_both - This type ends and begins with whitespace

External Requirements:
1) cleanCSS function - This stand alone application is the
   beautification engine used for the contents of "style" tags
2) js_beautify function - This stand alone application is the
   beautification engine used for the contents of "script" tags
3) markupmin - This application is the minifier for markup langauges.
   This is the first executed instruction so that we can start fresh
   without any unneeded trash that may provide interference.
   3a) markupmin requires the jsmin function for its standard
       minification, but that portion of markupmin is not used by
       markup_beauty

Arguments:
1) source - This is the source content to parse.
2) indent_size - This is the number of characters that make up a single
   indentation.  The default value is 4.
3) indent_character - This is the character used for indentation.  The
   default character is a space.
4) indent_comment - This determines whether comments should be indented
   in accordance with the markup or flush to the left.  The acceptable
   values are 'noindent' or 'indent'.  'noindent' is the default value.
5) indent_script - This determines whether the contents of script or
   style tags should be indented starting from the opening script or
   style tag or if this code should start indentation independent of the
   markup.  Acceptable values are true or false.  The default value is
   false.
6) presume_html - If this argument is supplied the boolean value true,
   and not string "true", then tags with certain tag names are converted
   to singleton types after the type assignment is performed.  This
   correction occurs regardless of syntax.

markup_summary
   The markup_summary function is defined at the very bottom of
   markup_beauty, but is not scoped by markup_beauty.  It is intended
   for use as closure to markup_beauty, because the variable name
   markup_summary is intended for use outside of markup_beauty, but it
   requires access to the variables and arrays of markup_beauty.
   
   Markup Summary is a small HTML table and some conditional warning
   statements to prodive statistics for analysis of processed markup and
   to alter users if to possibly flaws in the input that may likely
   interefere with processing of beautification.
------------------------------------------------------------------------
*/
var markup_beauty = function (source, indent_size, indent_character, indent_comment, indent_script, presume_html) {
    "use strict";
    var i, Z, tab = '',
    token = [],
    build = [],
    cinfo = [],
    level = [],
    inner = [],
    summary = [],
    x = source,
    loop,

    //innerset Function is the logic that intelligently identifies the
    //angle brackets nested inside quotes and converts them to square
    //brackets to prevent interference of beautification.  This is the
    //logic that allows JSP beautification to occur.
    innerset = (function () {
        var a, b, e, f, g, j, l, m, n, o, p, h = -1,
        i = 0,
        k = -1,
        c = source.length,
        d = [];
        for (a = 0; a < c; a += 1) {

            //if a PHP, ASP, script, or style found then pass over it
            if (x.substr(a, 7).toLowerCase() === "<script") {
                for (b = a + 7; b < c; b += 1) {
                    if (x.charAt(b) + x.charAt(b + 1) + x.charAt(b + 2).toLowerCase() + x.charAt(b + 3).toLowerCase() + x.charAt(b + 4).toLowerCase() + x.charAt(b + 5).toLowerCase() + x.charAt(b + 6).toLowerCase() + x.charAt(b + 7).toLowerCase() + x.charAt(b + 8) === "</script>") {
                        a = b + 8;
                        h += 1;
                        break;
                    }
                }
            } else if (x.substr(a, 6).toLowerCase() === "<style") {
                for (b = a + 6; b < c; b += 1) {
                    if (x.charAt(b) + x.charAt(b + 1) + x.charAt(b + 2).toLowerCase() + x.charAt(b + 3).toLowerCase() + x.charAt(b + 4).toLowerCase() + x.charAt(b + 5).toLowerCase() + x.charAt(b + 6).toLowerCase() + x.charAt(b + 7) === "</style>") {
                        a = b + 7;
                        h += 1;
                        break;
                    }
                }
            } else if (x.substr(a, 5) === "<?php") {
                for (b = a + 5; b < c; b += 1) {
                    if (x.charAt(b - 1) === "?" && x.charAt(b) === ">") {
                        a = b;
                        h += 1;
                        break;
                    }
                }
            } else if (x.charAt(a) === "<" && x.charAt(a + 1) === "%") {
                for (b = a + 5; b < c; b += 1) {
                    if (x.charAt(b - 1) === "%" && x.charAt(b) === ">") {
                        a = b;
                        h += 1;
                        break;
                    }
                }

                //Don't even bother with empty qutoes: "" or ''
            } else if (x.charAt(a) === x.charAt(a + 1) && (x.charAt(a) === "\"" || x.charAt(a) === "'")) {
                a += 1;
            } else if (x.charAt(a - 1) === "=" && (x.charAt(a) === "\"" || x.charAt(a) === "'")) {

                //This first bit with the "m" and "o" variables
                //instructs the principle loop of innerset to ignore
                //quote characters that fall outside of tags.
                o = -1;
                for (m = a - 1; m > 0; m -= 1) {
                    if ((x.charAt(m) === "\"" && x.charAt(a) === "\"") || (x.charAt(m) === "'" && x.charAt(a) === "'") || x.charAt(m) === "<") {
                        break;
                    } else if (x.charAt(m) === ">") {
                        o = m;
                        break;
                    }
                }

                if (o === -1) {
                    //n is reset to be used as a switch.
                    n = 0;
                    for (b = a + 1; b < c; b += 1) {

                        //Ignore closing quotes if they reside inside a
                        //script, style, ASP, or PHP block
                        if (x.substr(b, 7).toLowerCase() === "<script") {
                            for (p = b + 7; p < c; p += 1) {
                                if (x.charAt(p) + x.charAt(p + 1) + x.charAt(p + 2).toLowerCase() + x.charAt(p + 3).toLowerCase() + x.charAt(p + 4).toLowerCase() + x.charAt(p + 5).toLowerCase() + x.charAt(p + 6).toLowerCase() + x.charAt(p + 7).toLowerCase() + x.charAt(p + 8) === "</script>") {
                                    b = p + 8;
                                    break;
                                }
                            }
                        } else if (x.substr(b, 6).toLowerCase() === "<style") {
                            for (p = b + 6; p < c; p += 1) {
                                if (x.charAt(p) + x.charAt(p + 1) + x.charAt(p + 2).toLowerCase() + x.charAt(p + 3).toLowerCase() + x.charAt(p + 4).toLowerCase() + x.charAt(p + 5).toLowerCase() + x.charAt(p + 6).toLowerCase() + x.charAt(p + 7) === "</style>") {
                                    b = p + 7;
                                    break;
                                }
                            }
                        } else if (x.substr(b, 5) === "<?php") {
                            for (p = b + 5; p < c; p += 1) {
                                if (x.charAt(p - 1) === "?" && x.charAt(p) === ">") {
                                    b = p;
                                    break;
                                }
                            }
                        } else if (x.charAt(b) === "<" && x.charAt(b + 1) === "%") {
                            for (p = b + 5; p < c; p += 1) {
                                if (x.charAt(p - 1) === "%" && x.charAt(p) === ">") {
                                    b = p;
                                    break;
                                }
                            }
                        } else if (x.charAt(b) === ">" || x.charAt(b) === "<") {

                            //if there is no reason to push every set of
                            //quotes into the "d" array if those quotes
                            //do not contain angle brackets.  This is a
                            //switch to test for such.
                            n = 1;
                        } else if ((x.charAt(b - 1) !== "\\" && ((x.charAt(a) === "\"" && x.charAt(b) === "\"") || (x.charAt(a) === "'" && x.charAt(b) === "'"))) || b === c - 1) {
                            //The "l" variable is used as an on/off
                            //switch to allow content, but not
                            //sequentially.  Tags with quotes following
                            //content with quotes need to be decremented
                            //to correct an inflated count
                            if (k !== h && l === 1) {
                                l = 0;
                                h -= 1;
                                k -= 1;
                            } else if (k === h) {
                                for (e = i + 1; e > a; e += 1) {
                                    if (!/\s/.test(x.charAt(e))) {
                                        break;
                                    }
                                }
                                j = e;
                                //This condition is for nonsequential
                                //content pieces
                                if (i < a && l !== 1) {
                                    l = 1;
                                    h += 1;
                                    k += 1;
                                }
                            }
                            //a = index of opening quotes from a pair
                            //b = index of closing quotes from a pair
                            //h = tag and content count
                            //j = the index where tag "h" starts
                            if (n === 1) {
                                d.push([a, b, h, j]);
                            }
                            a = b;
                            break;
                        }
                    }
                }
            } else if (x.charAt(a) === "<") {
                //If a HTML/XML comment is encountered then skip ahead
                if (x.charAt(a + 1) === "!" && x.charAt(a + 2) === "-" && x.charAt(a + 3) === "-") {
                    for (b = a + 4; b < x.length; b += 1) {
                        if (x.charAt(b) === "-" && x.charAt(b + 1) === "-" && x.charAt(b + 2) === ">") {
                            break;
                        }
                    }
                    h += 1;
                    a = b + 2;
                    //If not a HTML/XML comment increase the tag count
                } else {
                    h += 1;
                    j = a;
                }
            } else if (x.charAt(a + 1) === "<" && x.charAt(a) !== ">") {
                //Acount for content outside of tags
                for (b = a; b > 0; b -= 1) {
                    if (!/\s/.test(x.charAt(b)) && x.charAt(b) !== ">") {
                        h += 1;
                        k += 1;
                        j = a;
                        break;
                    } else if (x.charAt(b) === ">") {
                        if (h !== k) {
                            k += 1;
                            i = a;
                        }
                        break;
                    }
                }
                //Count for the closing of tags
            } else if (x.charAt(a) === ">") {
                k += 1;
                i = a;
            }
        }
        c = d.length;
        x = x.split('');

        //Code hand off must occur between quote discovery and tag
        //count.  Hand off must allow for discovery to be repacked into
        //numbers relevant to postcomputation and not to input.
        //This hand off produces the "inner" array for consumption by
        //the innerfix array.
        for (a = 0; a < c; a += 1) {
            i = d[a][0] + 1;
            f = d[a][1];
            g = d[a][2];
            j = d[a][3];

            //This loop converts quotes angle brackets to square
            //brackets and simultaneously builds out the "inner" array.
            //The inner array contains the reference locations of the
            //converted angle brackets so the program can put the angle
            //brackets back after JavaScript and CSS are beautified.
            for (e = i; e < f; e += 1) {

                //h is the character index of a converted angle bracket
                //in a given tag
                h = 0;
                if (x[e] === "<") {
                    x[e] = "[";
                    for (b = e; b > j; b -= 1) {
                        h += 1;
                        if (/\s/.test(x[b])) {
                            for (k = b - 1; k > j; k -= 1) {
                                if (!/\s/.test(x[k])) {

                                    //This condition accounts for white
                                    //space normalization around equal
                                    //characters that is supplied by
                                    //markupmin, otherwise h is
                                    //incremented for runs of white
                                    //space characters accounting for
                                    //tokenization
                                    if (x[k] !== "=") {
                                        h += 1;
                                    } else if (/\s/.test(x[k - 1])) {
                                        h -= 1;
                                    }
                                    b = k;
                                    break;
                                }
                            }
                        }
                    }
                    if (/\s/.test(x[i])) {
                        h -= 1;
                    }
                    inner.push(["<", h, g]);
                } else if (x[e] === ">") {
                    x[e] = "]";
                    for (b = e; b > j; b -= 1) {
                        h += 1;
                        if (/\s/.test(x[b])) {
                            for (k = b - 1; k > j; k -= 1) {
                                if (!/\s/.test(x[k])) {
                                    if (x[k] !== "=") {
                                        h += 1;
                                    } else if (/\s/.test(x[k - 1])) {
                                        h -= 1;
                                    }
                                    b = k;
                                    break;
                                }
                            }
                        }
                    }
                    if (/\s/.test(x[i])) {
                        h -= 1;
                    }
                    inner.push([">", h, g]);
                }
            }
        }
        //x must be joined back into a string so that it can pass
        //through the markupmin function.
        x = x.join('');
    }()),

    //This function builds full tags and content into array elements
    //and names the tag types or content into elements of a separate
    //array.
    elements = (function () {
        var q, a,

        //This function looks for the end of a designated tag and
        //then returns the entire tag as a single output.
        b = function (end) {
            var d, e, f = '',
            c = i,
            z = end.charAt(end.length - 2),
            y = end.split('').reverse(),

            //The first loop looks for the end position of a tag.
            //The second loop verifies the first loop did not stop
            //too early.
            g = function () {
                for (; c < loop; c += 1) {
                    if (z !== "-" && z !== "?" && z !== "%" && x[c] === ">") {
                        break;
                    } else if (x[c - 1] + x[c] === z + ">") {
                        break;
                    }
                }
                Z = y.length;
                for (d = 0; d < Z; d += 1) {
                    if (x[c - d] !== y[d]) {
                        e = false;
                        c += 1;
                        break;
                    }
                    e = true;
                }
            };
            g();

            //This is a check to ensure the entire intended tag
            //was actually captured and stopped because of an
            //early ">" character.  This check is necessary for
            //comments, ASP, and PHP tags.
            if (e !== true) {
                do {
                    g();
                } while (e !== true);
            } else if (e === true) {

                //Concat the characters from the now know end point
                //to build the tag.
                Z = c + 1;
                for (d = i; d < Z; d += 1) {
                    f = f + x[d];
                }
            }

            //This logic provides the ability for later logic to
            //determine if a singleton tag must be treated as
            //content or as an element with indentation.
            if (x[i - 2] === ">" && x[i - 1] === " ") {
                f = " " + f;
            }
            i = c - 1;
            return f;
        },

        //This function builds content into isolated usable units.
        //This is for content while elements() is for tags.
        cgather = function (z) {
            var c, d = '';
            q = "";
            for (c = i; c < loop; c += 1) {

                //This logic verifies if the end script tag is quoted
                if (q === "" && x[c - 1] !== "\\" && (x[c] === "'" || x[c] === "\"")) {
                    q = x[c];
                } else if (x[c - 1] !== "\\" && ((q === "'" && x[c] === "'") || (q === "\"" && x[c] === "\""))) {
                    q = "";
                }
                if (((z === "script" && q === "") || z === "style") && x[c] === "<" && x[c + 1] === "/" && x[c + 2].toLowerCase() === "s") {
                    if (z === "script" && (x[c + 3].toLowerCase() === "c" && x[c + 4].toLowerCase() === "r" && x[c + 5].toLowerCase() === "i" && x[c + 6].toLowerCase() === "p" && x[c + 7].toLowerCase() === "t")) {
                        break;
                    } else if (z === "style" && (x[c + 3].toLowerCase() === "t" && x[c + 4].toLowerCase() === "y" && x[c + 5].toLowerCase() === "l" && x[c + 6].toLowerCase() === "e")) {
                        break;
                    }
                } else if (z === "other" && x[c] === "<") {
                    break;
                } else {
                    d = d + x[c];
                }
            }
            i = c - 1;
            return d;
        },

        //The type_define function sorts markup code into various
        //designated types.  The build array holds the particular
        //code while the token array holds the designation for that
        //code.  The argument supplied to the "b" function is the
        //syntax ending for a given tag type.
        //I have designed these types but others can be added:
        //   * SGML and XML tag comments
        //   * SSI Apache instructions
        //   * SGML declarations, such as the HTML Doctype
        //   * XML processing declarations
        //   * PHP tags - These tags must be <?php and not <?.
        //   * SCRIPT tags for html
        //   * STYLE tags for html
        //   * ASP tags
        //   * ending tags of a tag pair
        //   * singelton tags, such as <br/>, <meta/>, <link/>
        //   * starting tags of a tag pair
        //
        //   !Tags starting with only <? are not considered, so by
        //default these are treated as a start tag.  Use the correct
        //PHP tags!!!!
        //   !Singelton tags must end with "/>" or they will also be
        //regarded as start tags.  This code is vocabulary
        //independent and I do not read minds.
        type_define = (function () {

            //The source data is minified before it is beautified.
            x = markupmin(x, 'beautify', presume_html).split('');
            loop = x.length;

            for (i = 0; i < loop; i += 1) {
                if (x[i] === "<" && x[i + 1] === "!" && x[i + 2] === "-" && x[i + 3] === "-" && x[i + 4] !== "#") {
                    build.push(b('-->'));
                    token.push("T_comment");
                } else if (x[i] === "<" && x[i + 1] === "!" && x[i + 2] === "-" && x[i + 3] === "-" && x[i + 4] === "#") {
                    build.push(b('-->'));
                    token.push("T_ssi");
                } else if (x[i] === "<" && x[i + 1] === "!" && x[i + 2] !== "-") {
                    build.push(b('>'));
                    token.push("T_sgml");
                } else if (x[i] === "<" && x[i + 1] === "?" && x[i + 2].toLowerCase() === "x" && x[i + 3].toLowerCase() === "m" && x[i + 4].toLowerCase() === "l") {
                    build.push(b('?>'));
                    token.push("T_xml");
                } else if (x[i] === "<" && x[i + 1] === "?" && x[i + 2].toLowerCase() === "p" && x[i + 3].toLowerCase() === "h" && x[i + 4].toLowerCase() === "p") {
                    build.push(b('?>'));
                    token.push("T_php");
                } else if (x[i] === "<" && x[i + 1].toLowerCase() === "s" && x[i + 2].toLowerCase() === "c" && x[i + 3].toLowerCase() === "r" && x[i + 4].toLowerCase() === "i" && x[i + 5].toLowerCase() === "p" && x[i + 6].toLowerCase() === "t") {
                    build.push(b('>'));
                    token.push("T_script");
                } else if (x[i] === "<" && x[i + 1].toLowerCase() === "s" && x[i + 2].toLowerCase() === "t" && x[i + 3].toLowerCase() === "y" && x[i + 4].toLowerCase() === "l" && x[i + 5].toLowerCase() === "e") {
                    build.push(b('>'));
                    token.push("T_style");
                } else if (x[i] === "<" && x[i + 1] === "%") {
                    build.push(b('%>'));
                    token.push("T_asp");
                } else if (x[i] === "<" && x[i + 1] === "/") {
                    build.push(b('>'));
                    token.push("T_tag_end");
                } else if (x[i] === "<" && (x[i + 1] !== "!" || x[i + 1] !== "?" || x[i + 1] !== "/" || x[i + 1] !== "%")) {
                    for (a = i; a < loop; a += 1) {
                        if (x[a] === "/" && x[a + 1] === ">") {
                            build.push(b('/>'));
                            token.push("T_singleton");
                            break;
                        } else if (x[a] !== "/" && x[a + 1] === ">") {
                            build.push(b('>'));
                            token.push("T_tag_start");
                            break;
                        }
                    }
                } else if (x[i - 1] === ">" && (x[i] !== "<" || (x[i] !== " " && x[i + 1] !== "<"))) {
                    if (token[token.length - 1] === "T_script") {
                        build.push(cgather("script"));
                        token.push("T_content");
                    } else if (token[token.length - 1] === "T_style") {
                        build.push(cgather("style"));
                        token.push("T_content");
                    } else if (x[i - 1] + x[i] + x[i + 1] !== "> <") {
                        build.push(cgather("other"));
                        token.push("T_content");
                    }
                }
            }
        }());
        //summary is a replica of the build array prior to any
        //beautification for use in the markup_summary function
        summary = summary.concat(build);
    }()),

    //This function provides structual relevant descriptions for
    //content and groups tags into categories.
    code_type = (function () {
        Z = token.length;
        for (i = 0; i < Z; i += 1) {
            if (token[i] === "T_sgml" || token[i] === "T_xml") {
                cinfo.push("parse");
            } else if (token[i] === "T_asp" || token[i] === "T_php" || token[i] === "T_ssi") {
                cinfo.push("singleton");
            } else if (token[i] === "T_comment") {
                cinfo.push("comment");
            } else if ((token[i] === "T_content" && build[i] !== " ") && token[i - 1] === "T_script") {
                cinfo.push("external");
            } else if (token[i] === "T_content" && token[i - 1] === "T_style") {
                cinfo.push("external");
            } else if (token[i] === "T_content" && build[i].charAt(0) === " " && build[i].charAt(build[i].length - 1) === " ") {
                cinfo.push("mixed_both");
            } else if (token[i] === "T_content" && build[i].charAt(0) === " " && build[i].charAt(build[i].length - 1) !== " ") {
                cinfo.push("mixed_start");
            } else if (token[i] === "T_content" && build[i].charAt(0) !== " " && build[i].charAt(build[i].length - 1) === " ") {
                cinfo.push("mixed_end");
            } else if (token[i] === "T_content") {
                cinfo.push("content");
            } else if (token[i] === "T_tag_start") {
                cinfo.push("start");
            } else if (token[i] === "T_style") {
                build[i] = build[i].replace(/\s+/g, " ");
                cinfo.push("start");
            } else if (token[i] === "T_script") {
                build[i] = build[i].replace(/\s+/g, " ");
                cinfo.push("start");
            } else if (token[i] === "T_singleton") {
                cinfo.push("singleton");
            } else if (token[i] === "T_tag_end") {
                cinfo.push("end");
            }
        }
    }()),

    //The tag_check function creates the tab stops from the values
    //supplied by the indent_character and indent_size arguments.
    //If no values are supplied or are supplied improperly a
    //reasonable default is created.
    tab_check = (function () {
        var a;
        indent_size = Number(indent_size);
        if (indent_size !== Number(indent_size)) {
            indent_size = 4;
        }
        Z = indent_size;
        for (a = 0; a < Z; a += 1) {
            tab += indent_character;
        }
        return tab;
    }()),

    //this function cheats the structure and looks at tag names
    cheat = (function () {
        if (presume_html !== true) {
            return;
        }
        var a, b;
        loop = cinfo.length;
        for (i = 0; i < loop; i += 1) {
            if (cinfo[i] === "start") {
                a = build[i].indexOf(" ");
                if (build[i].length === 3) {
                    b = build[i].charAt(1).toLowerCase();
                } else if (a === -1) {
                    b = build[i].slice(1, cinfo[i].length - 2).toLowerCase();
                } else if (a === 0) {
                    b = build[i].slice(1, build[i].length);
                    a = b.indexOf(" ");
                    b = b.slice(1, a).toLowerCase();
                } else {
                    b = build[i].slice(1, a).toLowerCase();
                }
                if (b === "br" || b === "meta" || b === "link" || b === "img" || b === "hr" || b === "base" || b === "basefont" || b === "area" || b === "col" || b === "frame" || b === "input" || b === "param") {
                    cinfo[i] = "singleton";
                    token[i] = "T_singleton";
                }
            }
        }
    }()),
    //This function sets the tab levels for the code.  It is set to
    //use the cinfo array definitions but could be rewritten to use
    //the token array.
    tab_level = (function () {
        loop = cinfo.length;
        var a,

        //This function looks back to the most previous indented tag
        //that is not an end tag and returns a count based upon the
        //number between the current tag and this previous indented tag.
        //If the argument has a value of "start" then indentation is
        //increased by 1.
        c = function (x) {
            var k, m = 0;
            if (x === "start") {
                m += 1;
            }
            for (k = i - 1; k > -1; k -= 1) {
                if (cinfo[k] === "start" && level[k] === "x") {
                    m += 1;
                } else if (cinfo[k] === "end") {
                    m -= 1;
                } else if (cinfo[k] === "start" && level[k] !== "x") {
                    return level.push(level[k] + m);
                } else if (k === 0) {
                    if (cinfo[k] !== "start") {
                        return level.push(0);
                    } else if (cinfo[i] === "mixed_start" || cinfo[i] === "content" || (cinfo[i] === "singleton" && build[i].charAt(0) !== " ")) {
                        return level.push("x");
                    } else {
                        return level.push(1);
                    }
                }
            }
        },

        //This function is used by end tags to determine
        //indentation.
        e = function () {
            var yy = 1,

            //This executed if the previous start is not indented.
            z = function (y) {
                for (; y > 0; y -= 1) {
                    if (level[y] !== "x") {
                        return level.push(level[y] + 1);
                    }
                }
            },

            //If the prior item is an end tag or content ending with
            //a space black box voodoo magic must occur.
            w = function () {
                var k, q, y = i - 1,

                //This function finds the prior existing indented
                //start tag.  This start tag may not be the current
                //end tag's matching pair.  If the tag prior to this
                //indented start tag is not an end tag this function
                //escapes and later logic is used.
                u = function () {
                    //t() is executed if the prior indented start tag
                    //exists directly after an end tag.  This function
                    //is necessary to determine if indentation must be
                    //subtracted from the prior indented start tag.
                    var t = function () {
                        var s, l = 0;

                        //Finds the prior start tag followed by a start
                        //tag where both have indentation.  This creates
                        //a frame of reference for performing reflexive
                        //calculation.
                        for (s = i - 1; s > 0; s -= 1) {
                            if ((cinfo[s] === "start" && cinfo[s + 1] === "start" && level[s] === level[s + 1] - 1) || (cinfo[s] === "start" && cinfo[s - 1] !== "start" && level[s] === level[s - 1])) {
                                break;
                            }
                        }

                        //The incrementor is increased if indented
                        //content found followed by unindented end tag
                        //by looping up from the frame of reference.
                        for (k = s + 1; k < i; k += 1) {
                            if (cinfo[k] === "mixed_start" && cinfo[k + 1] === "end") {
                                l += 1;
                            }
                        }

                        //If the prior logic fails and frame of
                        //reference follows an indented end tag the
                        //incrementor is increased.
                        if (cinfo[s - 1] === "end" && level[s - 1] !== "x" && l === 0) {
                            l += 1;
                        }

                        //All the prior logic can fail and so a
                        //redundant check was added.
                        if (l !== 0) {
                            if (level[i - 1] === "x") {
                                return l - 1;
                            } else {
                                return l;
                            }
                        } else {
                            for (; s < i; s += 1) {
                                if (cinfo[s] === "start") {
                                    l += 1;
                                } else if (cinfo[s] === "end") {
                                    l -= 1;
                                }
                            }
                            return l;
                        }
                    };
                    for (; y > 0; y -= 1) {
                        if (cinfo[y] !== "mixed_end" || (cinfo[y] === "start" && level[y] !== "x")) {
                            if (cinfo[y - 1] === "end") {
                                q = "r";
                                if (cinfo[i - 1] === "mixed_both" && level[i - 1] === level[y] - t()) {
                                    return level.push(level[y] - (t() + 1));
                                } else if (cinfo[i - 2] === "start" && (cinfo[i - 1] === "mixed_end" || cinfo[i - 1] === "mixed_both")) {
                                    return level.push(level[y]);
                                } else if (level[y] !== "x") {
                                    if (cinfo[y] === "mixed_both" && y !== i - t()) {
                                        if (y === i - 1) {
                                            return level.push(level[y] - 1);
                                        } else {
                                            return level.push(level[y] + t());
                                        }
                                    } else if (cinfo[i - 1] === "mixed_end" && t() === 0) {
                                        return level.push(level[y] - 1);
                                    } else {
                                        return level.push(level[y] - t());
                                    }
                                }
                            } else {
                                q = y;
                                return;
                            }
                        }
                    }
                },

                //This seeks to find a frame of reference by looking
                //for the first start tag outside a counted pair not
                //counting the current end tag.
                r = function () {
                    var l = 0;
                    for (k = i; k > 0; k -= 1) {
                        if (cinfo[k] === "end") {
                            l += 1;
                        } else if (cinfo[k] === "start") {
                            l -= 1;
                        }
                        if (l === 0) {
                            return k;
                        }
                    }
                };

                //If the prior two elements are an empty pair.
                if (cinfo[i - 1] === "end" && level[i - 1] !== "x") {
                    if (cinfo[i - 2] === "start" && level[i - 2] === "x") {
                        for (k = i - 2; k > 0; k -= 1) {
                            if (level[k] !== "x") {
                                break;
                            }
                        }
                        if (cinfo[k] === "start") {
                            return c("end");
                        } else {
                            return level.push(level[k] - 1);
                        }
                    } else if (cinfo[i - 2] === "start" && level[i - 2] !== "x") {
                        return level.push(level[i - 2] - 1);
                    } else {
                        return level.push(level[i - 1] - 1);
                    }

                    //If the prior two elements are not an empty
                    //pair voodoo magic must occur.
                } else {

                    //u() makes a context decision based upon the
                    //placement of the current end tag relevant to
                    //the prior indented start tag.
                    u();
                    if (q === "r") {
                        return;
                    } else {
                        y = 0;
                        for (q = r(); q > 0; q -= 1) {
                            if (cinfo[q] === "start") {
                                y += 1;
                            } else if (cinfo[q] === "end") {
                                y -= 1;
                            }
                            if (level[q] !== "x") {
                                if (cinfo[q] === "end" && cinfo[q - 1] === "start" && level[q - 1] !== "x") {
                                    return level.push(level[q]);
                                } else if (level[i - 1] === "x" && build[i].charAt(0) !== " " && cinfo[i - 1] !== "mixed_end" && (cinfo[i - 2] !== "end" || level[i - 2] !== "x") && (cinfo[i - 3] !== "end" || level[i - 3] !== "x")) {
                                    return level.push("x");
                                } else {
                                    return level.push(level[q] + (y - 1));
                                }
                            }
                        }
                        y = 0;
                        for (q = i; q > -1; q -= 1) {
                            if (cinfo[q] === "start") {
                                y += 1;
                            } else if (cinfo[q] === "end") {
                                y -= 1;
                            }
                        }
                        return level.push(y);
                    }
                }
            };

            if (cinfo[i - 1] === "end" || cinfo[i - 1] === "mixed_both" || cinfo[i - 1] === "mixed_end") {
                return w();
            } else if (cinfo[i - 1] === "mixed_start" || cinfo[i - 1] === "content") {
                return level.push("x");
            } else if (cinfo[i - 1] === "external") {
                yy = -1;
                for (a = i - 2; a > 0; a -= 1) {
                    if (cinfo[a] === "start") {
                        yy += 1;
                    } else if (cinfo[a] === "end") {
                        yy -= 1;
                    }
                    if (level[a] !== "x") {
                        break;
                    }
                }
                if (cinfo[a] === "end") {
                    yy += 1;
                }
                return level.push(level[a] + yy);
            } else if (build[i].charAt(0) !== " ") {
                if ((cinfo[i - 1] === "singleton" || cinfo[i - 1] === "content") && level[i - 1] === "x") {
                    return level.push("x");
                }
                yy = 0;
                for (a = i - 1; a > 0; a -= 1) {
                    //Find the previous indention and if not a start
                    if (cinfo[a] === "singleton" && level[a] === "x" && ((cinfo[a - 1] === "singleton" && level[a - 1] !== "x") || cinfo[a - 1] !== "singleton")) {
                        yy += 1;
                    }
                    if (level[a] !== 0 && level[a] !== "x" && cinfo[i - 1] !== "start") {
                        if (cinfo[a] === "mixed_both" || cinfo[a] === "mixed_start") {
                            return level.push(level[a] - yy);
                        } else if (level[a] === yy || (cinfo[a] === "singleton" && (cinfo[a - 1] === "content" || cinfo[a - 1] === "mixed_start"))) {
                            return level.push(level[a]);
                        } else {
                            return level.push(level[a] - 1);
                        }
                        //Find the previous start that is not indented
                    } else if (cinfo[a] === "start" && level[a] === "x") {
                        return z(a);
                        //If the previous tag is an indented start
                    } else if (cinfo[i - 1] === "start") {
                        return level.push(level[a]);
                    }
                }
                return level.push(0);
            } else {
                return c("end");
            }
        },

        //This function is used by cinfo values of "start" and
        //"singleton" through the "g" function.
        f = function (z) {

            //The n() function is only a container.  It sets the
            //values of k, l, m.  If not a comment k = i - 1,
            //and if not a comment l = k - i, and if not a comment
            //m = l - 1.
            var k, l, m, n = (function () {
                var j;
                if (z === 1) {
                    k = 0;
                    l = 0;
                    m = 0;
                } else {
                    for (j = z - 1; j > 0; j -= 1) {
                        if (cinfo[j] !== "comment") {
                            k = j;
                            break;
                        }
                    }
                    if (k === 1) {
                        l = 0;
                        m = 0;
                    } else {
                        for (j = k - 1; j > 0; j -= 1) {
                            if (cinfo[j] !== "comment") {
                                l = j;
                                break;
                            }
                        }
                        if (l === 1) {
                            m = 0;
                        } else {
                            for (j = l - 1; j > 0; j -= 1) {
                                if (cinfo[j] !== "comment") {
                                    m = j;
                                    break;
                                }
                            }
                        }
                    }
                }
            }()),

            //This is executed if the prior non-comment item is not
            //any form of content and is indented.
            p = function () {
                var j, v = 1,
                u = -1;
                for (j = k; j > 0; j -= 1) {
                    if (cinfo[j] === "start") {
                        u -= 1;
                        if (level[j] === "x") {
                            v += 1;
                        }
                    } else if (cinfo[j] === "end") {
                        u += 1;
                        v -= 1;
                    }
                    if (level[j] === 0) {
                        k = 0;
                        for (l = i - 1; l > j; l -= 1) {
                            if (cinfo[l] === "start") {
                                k += 1;
                            } else if (cinfo[l] === "end") {
                                k -= 1;
                            }
                        }
                        if (k > 0) {
                            if (level[j + 1] === "x") {
                                return level.push(((u) * -1) - 1);
                            } else if (cinfo[j] !== "external") {
                                return level.push((u + 1) * -1);
                            }
                        } else {
                            for (k = i - 1; k > 0; k -= 1) {
                                if (level[k] !== "x") {
                                    return level.push(level[k]);
                                }
                            }
                        }
                    }
                    if (level[j] !== "x" && level[i - 1] !== "x") {
                        if (cinfo[j] === "start" || cinfo[j] === "end") {
                            return level.push(level[j] + v);
                        } else {
                            return level.push(level[j] + v - 1);
                        }
                    } else if (u === -1 && level[j] === "x") {
                        break;
                    } else if (u === 1 && level[j] !== "x" && cinfo[j] !== "mixed_start" && cinfo[j] !== "content") {
                        if (cinfo[j - 1] === "mixed_end" || (level[i - 1] === "x" && cinfo[i - 1] === "end" && cinfo[j] !== "end")) {
                            return level.push(level[j] - u - 1);
                        } else {
                            return level.push(level[j] - u);
                        }
                    } else if (u === 0 && level[j] !== "x") {
                        return c("start");
                    }
                }
                return c("start");
            };

            //A one time fail safe to prevent a referential anomoly.
            if (i - 1 === 0 && cinfo[0] === "start") {
                return level.push(1);

                //For a tag to become void of whitespace cushioning
            } else if (cinfo[k] === "mixed_start" || cinfo[k] === "content" || cinfo[i - 1] === "mixed_start" || cinfo[i - 1] === "content" || (cinfo[i] === "singleton" && (cinfo[i - 1] === "start" || cinfo[i - 1] === "singleton") && build[i].charAt(0) !== " ")) {
                return level.push("x");

                //Simple regular tabbing
            } else if ((cinfo[i - 1] === "comment" && level[i - 1] === 0) || ((cinfo[m] === "mixed_start" || cinfo[m] === "content") && cinfo[l] === "end" && (cinfo[k] === "mixed_end" || cinfo[k] === "mixed_both"))) {
                return c("start");

                //if the prior item is an indented comment then go with it
            } else if (cinfo[i - 1] === "comment" && level[i - 1] !== "x") {
                return level.push(level[i - 1]);
            } else if ((cinfo[k] === "start" && level[k] === "x") || (cinfo[k] !== "mixed_end" && cinfo[k] !== "mixed_both" && level[k] === "x")) {
                if (level[i - 1] === "x" && build[i].charAt(0) !== " " && cinfo[i - 1] !== "start" && build[i - 1].charAt(build[i - 1].length - 1) !== " ") {
                    if ((cinfo[i - 1] === "end" && cinfo[i - 2] === "end") || (cinfo[i - 1] === "end" && cinfo[i] !== "end" && cinfo[i + 1] !== "mixed_start" && cinfo[i + 1] !== "content")) {
                        return c("start");
                    } else {
                        return level.push("x");
                    }
                } else {
                    return p();
                }
            } else if (cinfo[k] === "end" && level[k] !== "x" && (cinfo[k - 1] !== "start" || (cinfo[k - 1] === "start" && level[k - 1] !== "x"))) {
                if (level[k] < 0) {
                    return c("start");
                } else {
                    return level.push(level[k]);
                }
            } else if (cinfo[m] !== "mixed_start" && cinfo[m] !== "content" && (cinfo[k] === "mixed_end" || cinfo[k] === "mixed_both")) {
                l = 0;
                p = 0;
                m = 0;
                for (a = k; a > 0; a -= 1) {
                    if (cinfo[a] === "end") {
                        l += 1;
                    }
                    if (cinfo[a] === "start") {
                        p += 1;
                    }
                    if (level[a] === 0 && a !== 0) {
                        m = a;
                    }
                    if (cinfo[k] === "mixed_both" && level[a] !== "x") {
                        return level.push(level[a]);
                    } else if (cinfo[a] !== "comment" && cinfo[a] !== "content" && cinfo[a] !== "external" && cinfo[a] !== "mixed_end" && level[a] !== "x") {
                        if (cinfo[a] === "start" && level[a] !== "x") {
                            if (cinfo[i - 1] !== "end") {
                                return level.push(level[a] + (p - l));
                            } else if ((level[a] === level[a - 1] && cinfo[a - 1] !== "end" && level[a + 1] !== "x") || (cinfo[i - 2] === "start" && level[i - 2] !== "x" && level[i - 1] === "x")) {
                                return level.push(level[a] + 1);
                            } else if (p <= 1) {
                                return level.push(level[a]);
                            }
                        } else if (l > 0) {
                            if (p > 1) {
                                if (m !== 0) {
                                    return c("start");
                                } else {
                                    return level.push(level[a] + 1);
                                }
                            } else {
                                return level.push(level[a] - l + 1);
                            }
                        } else {
                            return level.push(level[a] + p);
                        }
                    }
                }
            } else if (cinfo[k] === "start" && level[k] !== "x") {
                //This section looks for the most previous level that is
                //not set for the noted cinfo values.  Once that value
                //is found it is added to the level array increased plus
                //1.
                for (a = i - 1; a > -1; a -= 1) {
                    if (cinfo[a] !== "comment" && cinfo[a] !== "content" && cinfo[a] !== "external" && cinfo[a] !== "mixed_end") {
                        if (cinfo[i + 1] && build[i].charAt(0) !== " " && (cinfo[i + 1] === "content" || cinfo[i + 1] === "mixed_end")) {
                            return level.push("x");
                        } else {
                            return level.push(level[a] + 1);
                        }
                    }
                }
                return level.push(0);
            } else {
                return c("start");
            }
        },

        //This merely verifies if a singleton element is used as
        //content.
        h = function () {
            var z;
            if (cinfo[i] !== "start" && level[i - 1] === "x" && cinfo[i - 1] !== "content" && build[i].charAt(0) !== " " && cinfo[i - 1] !== "mixed_start" && cinfo[i - 1] !== "mixed_end") {
                return level.push("x");
            } else if (cinfo[i] !== "start" && build[i] === " ") {
                build[i] = "";
                return level.push("x");
            } else {
                //This section corrects a calculation malfunction that
                //only occurs to start type elements if they occur
                //directly prior to a comment.  This function is
                //executed through h().
                if (cinfo[i - 1] !== "comment") {
                    f(i);
                } else {
                    for (z = i - 1; z > 0; z -= 1) {
                        if (cinfo[z] !== "comment") {
                            break;
                        }
                    }
                    f(z + 1);
                }
            }
        },

        //innerfix function undoes all the changes made by the innerfix
        //function.  This is what allows nested tags, such as JSP tags
        //to be beautified.
        innerfix = (function () {
            var a, b, c, d, e = inner.length;
            for (a = 0; a < e; a += 1) {
                b = inner[a][0];
                c = inner[a][1];
                d = inner[a][2];
                if (build[d].charAt(0) === " ") {
                    c += 1;
                }
                build[d] = build[d].split('');
                if (b === "<" && build[d][c] === "[") {
                    build[d][c] = "<";
                } else if (b === ">" && build[d][c] === "]") {
                    build[d][c] = ">";
                }
                build[d] = build[d].join('');
            }
        }()),

        //This logic only serves to assign the previously defined
        //subfunctions to each of the cinfo values.
        algorithm = (function () {
            var test = 0,
            test1 = 0,
            scriptStart = (/^(\s*<\!\-\-)/),
            scriptEnd = (/(\-\->\s*)$/);
            for (i = 0; i < loop; i += 1) {
                if (i === 0) {
                    level.push(0);
                } else if (cinfo[i] === "comment" && indent_comment !== 'noindent') {
                    h();
                } else if (cinfo[i] === "comment" && indent_comment === 'noindent') {
                    level.push(0);
                } else if (cinfo[i] === "external") {
                    level.push(0);
                    if (token[i - 1] === "T_script") {

                        //If the script begins with an HTML comment then
                        //that comment must be accounted for, removed,
                        //and returned.  Starting script with an HTML
                        //comment can confuse markupmin, so they have to
                        //be temporarily removed.  This logic is
                        //repeated for CSS styles.
                        if (scriptStart.test(build[i])) {
                            test = 1;
                        }
                        if (scriptEnd.test(build[i])) {
                            test1 = 1;
                        }
                        build[i] = js_beautify(build[i].replace(scriptStart, "").replace(scriptEnd, ""), 4, " ", true, 1, 0, true, false, false, indent_comment);
                        if (test === 1) {
                            build[i] = "<!--\n" + build[i];
                        }
                        if (test1 === 1) {
                            build[i] = build[i] + "\n-->";
                        }
                        build[i] = build[i].replace(/(\/\/(\s)+\-\->(\s)*)$/, "//-->");
                    } else if (token[i - 1] === "T_style") {
                        if (scriptStart.test(build[i])) {
                            test = 1;
                        }
                        if (scriptEnd.test(build[i])) {
                            test1 = 1;
                        }
                        build[i] = cleanCSS(build[i].replace(scriptStart, "").replace(scriptEnd, ""), indent_size, indent_character, true);
                        if (test === 1) {
                            build[i] = "<!--\n" + build[i];
                        }
                        if (test1 === 1) {
                            build[i] = build[i] + "\n-->";
                        }
                    }
                } else if (cinfo[i] === "content") {
                    level.push("x");
                } else if (cinfo[i] === "parse") {
                    h();
                } else if (cinfo[i] === "mixed_both") {
                    //The next merely removes the space at the front and
                    //back
                    h();
                } else if (cinfo[i] === "mixed_start") {
                    //The next line removes the space at the front
                    h();
                } else if (cinfo[i] === "mixed_end") {
                    //The next line removes the space at the end
                    build[i] = build[i].slice(0, build[i].length - 1);
                    level.push("x");
                } else if (cinfo[i] === "start") {
                    h();
                } else if (cinfo[i] === "end") {
                    e();
                } else if (cinfo[i] === "singleton") {
                    h();
                }
            }
        }());
    }()),

    //This function writes the indentation to those elements in the
    //build array that need to be indented.  The length of
    //indentation is designated by the values in the level array.
    write_tabs = (function () {
        var a, indent = '',

        //This function writes the standard indentation output
        tab_math = function (x) {
            for (a = 0; a < level[i]; a += 1) {
                indent += tab;
            }
            if (cinfo[i] === "mixed_both") {
                x = x.slice(0, x.length - 1);
            }
            x = "\n" + indent + x;
            indent = '';
            return x;
        },

        //This function writes the indentation output for cinfo
        //values of "end".  This function is different in that some
        //end elements do not receive indentation.
        end_math = function (x) {
            var b;
            if (cinfo[i - 1] !== "start") {
                for (b = i; b > 0; b -= 1) {
                    if (level[b] !== "x") {
                        break;
                    }
                }
                for (a = 1; a < level[b] + 1; a += 1) {
                    indent += tab;
                }
                x = "\n" + indent + x;
                indent = '';
            }
            return x;
        },

        script_math = function (x) {
            var b, c;
            a = 0;
            if (level[i - 1] === "x") {
                for (b = i - 1; b > 0; b -= 1) {
                    if (cinfo[b] === "start") {
                        a += 1;
                    } else if (cinfo[b] === "end") {
                        a -= 1;
                    }
                    if (level[b] !== "x") {
                        break;
                    }
                }
                if (cinfo[b] === "end") {
                    a += 1;
                }
                for (c = 0; c < level[b] + a; c += 1) {
                    indent += tab;
                }
            } else {
                for (c = 0; c < level[i - 1] + 1; c += 1) {
                    indent += tab;
                }
            }
            x = "\n" + indent + x.replace(/\n/g, "\n" + indent);
            indent = '';
            return x;
        };

        //This is the logic for assigning execution of the prior
        //three functions.
        loop = build.length;
        for (i = 1; i < loop; i += 1) {
            if (cinfo[i] === "end" && (cinfo[i - 1] !== "content" && cinfo[i - 1] !== "mixed_start")) {
                if (build[i].charAt(0) === " ") {
                    build[i] = build[i].substr(1);
                }
                if (level[i] !== "x") {
                    build[i] = end_math(build[i]);
                }
            } else if (cinfo[i] === "external" && indent_script === "indent") {
                build[i] = script_math(build[i]);
            } else if (level[i] !== "x" && (cinfo[i - 1] !== "content" && cinfo[i - 1] !== "mixed_start")) {
                if (build[i].charAt(0) === " ") {
                    build[i] = build[i].substr(1);
                }
                build[i] = tab_math(build[i]);
            }
        }
    }());
    markup_summary = function () {
        var a, b = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
        c,
        d = build.join('').length,
        e = source.length,
        f,
        g,
        h,
        i = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
        j,
        k,
        l,
        m = [],
        n = [],
        z = cinfo.length,
        insertComma = function (x) {
            if (typeof (x) === "number") {
                x = x.toString();
            }
            if (typeof (x) !== "string") {
                return x;
            }
            x = x.split("").reverse();
            z = x.length;
            for (a = 2; a < z; a += 3) {
                x[a] = "," + x[a];
            }
            x = x.reverse().join("");
            if (x.charAt(0) === ",") {
                x = x.slice(1, x.length);
            }
            return x;
        },
        zipf = function () {
            var a, b, k, w, z = cinfo.length,
            x = "",
            h = [],
            g = [],
            punctuation = function (y) {
                return y.replace(/(\,|\.|\?|\!|\:) /, " ");
            };
            for (a = 0; a < z; a += 1) {
                if (cinfo[a] === "content") {
                    x += " " + build[a];
                } else if (cinfo[a] === "mixed_start") {
                    x += build[a];
                } else if (cinfo[a] === "mixed_both") {
                    x += build[a].substr(0, build[a].length - 1);
                } else if (cinfo[a] === "mixed_end") {
                    x += " " + build[a].substr(0, build[a].length - 1);
                }
            }
            if (x.length === 0) {
                return "";
            }
            x = x.substr(1, x.length).toLowerCase();
            w = x.replace(/\&nbsp;?/gi, " ").replace(/[a-z](\,|\.|\?|\!|\:) /gi, punctuation).replace(/(\(|\)|"|\{|\}|\[|\])/g, "").replace(/ +/g, " ").split(" ");
            x = x.split(" ");
            z = w.length;
            for (a = 0; a < z; a += 1) {
                if (w[a] !== "") {
                    h.push([1, w[a]]);
                    for (b = a + 1; b < z; b += 1) {
                        if (w[b] === w[a]) {
                            h[h.length - 1][0] += 1;
                            w[b] = "";
                        }
                    }
                }
            }
            z = h.length;
            for (a = 0; a < z; a += 1) {
                k = a;
                for (b = a + 1; b < z; b += 1) {
                    if (h[b][0] > h[k][0] && h[b][1] !== "") {
                        k = b;
                    }
                }
                g.push(h[k]);
                if (h[k] !== h[a]) {
                    h[k] = h[a];
                } else {
                    h[k] = [0, ""];
                }
                if (g.length === 11) {
                    break;
                }
            }
            if (g.length < 2) {
                return "";
            } else if (g.length > 10) {
                b = 10;
            } else {
                b = g.length - 1;
            }
            for (a = 0; a < b; a += 1) {
                h[a] = (g[a][0] / g[a + 1][0]).toFixed(2);
                g[a] = "<tr><th>" + (a + 1) + "</th><td>" + g[a][1] + "</td><td>" + g[a][0] + "</td><td>" + h[a] + "</td><td>" + ((g[a][0] / x.length) * 100).toFixed(2) + "%</td></tr>";
            }
            g[g.length - 1] = "";
            return "<table class='analysis' summary=\"Zipf's Law\"><caption>This table demonstrates <em>Zipf's Law</em> by listing the 10 most occuring words in the content and the number of times they occurred.</caption><thead><tr><th>Word Rank</th><th>Most Occurring Word by Rank</th><th>Number of Instances</th><th>Ratio Increased Over Next Most Frequence Occurance</th><th>Percentage from " + insertComma(x.length) + " Total Word</th></tr></thead><tbody>" + g.join("") + "</tbody></table>";
        };
        for (a = 0; a < z; a += 1) {
            switch (cinfo[a]) {
            case "end":
                b[1] += 1;
                i[1] += summary[a].length;
                break;
            case "singleton":
                b[2] += 1;
                i[2] += summary[a].length;
                if (((build[a].indexOf("<embed ") !== -1 || build[a].indexOf("<img ") !== -1 || build[a].indexOf("<iframe ") !== -1) && (build[a].indexOf("src") !== -1 && build[a].indexOf("src=\"\"") === -1 && build[a].indexOf("src=''") === -1)) || (build[a].indexOf("<link ") !== -1 && build[a].indexOf("rel") !== -1 && build[a].indexOf("canonical") === -1)) {
                    m.push(build[a]);
                }
                break;
            case "comment":
                b[3] += 1;
                i[3] += summary[a].length;
                break;
            case "content":
                b[4] += 1;
                i[4] += summary[a].length;
                break;
            case "mixed_start":
                b[5] += 1;
                i[5] += summary[a].length;
                break;
            case "mixed_end":
                b[6] += 1;
                i[6] += summary[a].length;
                break;
            case "mixed_both":
                b[7] += 1;
                i[7] += summary[a].length;
                break;
            case "parse":
                b[10] += 1;
                i[10] += summary[a].length;
                break;
            case "external":
                b[17] += 1;
                i[17] += summary[a].length;
                if (((build[a].indexOf("<sc") !== -1 || build[a].indexOf("<embed ") !== -1 || build[a].indexOf("<img ") !== -1 || build[a].indexOf("<iframe ") !== -1) && (build[a].indexOf("src") !== -1 && build[a].indexOf("src=\"\"") === -1 && build[a].indexOf("src=''") === -1)) || (build[a].indexOf("<link ") !== -1 && build[a].indexOf("rel") !== -1 && build[a].indexOf("canonical") === -1)) {
                    m.push(build[a]);
                }
                break;
            }
            switch (token[a]) {
            case "T_tag_start":
                b[0] += 1;
                i[0] += summary[a].length;
                if (((build[a].indexOf("<embed ") !== -1 || build[a].indexOf("<img ") !== -1 || build[a].indexOf("<iframe ") !== -1) && (build[a].indexOf("src") !== -1 && build[a].indexOf("src=\"\"") === -1 && build[a].indexOf("src=''") === -1)) || (build[a].indexOf("<link ") !== -1 && build[a].indexOf("rel") !== -1 && build[a].indexOf("canonical") === -1)) {
                    m.push(build[a]);
                }
                break;
            case "T_sgml":
                b[8] += 1;
                i[8] += summary[a].length;
                break;
            case "T_xml":
                b[9] += 1;
                i[9] += summary[a].length;
                break;
            case "T_ssi":
                b[11] += 1;
                i[11] += summary[a].length;
                break;
            case "T_asp":
                b[12] += 1;
                i[12] += summary[a].length;
                break;
            case "T_php":
                b[13] += 1;
                i[13] += summary[a].length;
                break;
            case "T_script":
                b[15] += 1;
                i[15] += summary[a].length;
                if (build[a].indexOf(" src") !== -1) {
                    m.push(build[a]);
                }
                break;
            case "T_style":
                b[16] += 1;
                i[16] += summary[a].length;
                break;
            }
        }
        f = [b[0] + b[1] + b[2] + b[3], b[4] + b[5] + b[6] + b[7], b[15] + b[16] + b[17], b[11] + b[12] + b[13]];
        j = [i[0] + i[1] + i[2] + i[3], i[4] + i[5] + i[6] + i[7], i[15] + i[16] + i[17], i[11] + i[12] + i[13]];
        g = [f[0], f[0], f[0], f[0], f[1], f[1], f[1], f[1], b[10], b[10], b[10], f[3], f[3], f[3], f[3], f[2], f[2], f[2]];
        k = [j[0], j[0], j[0], j[0], j[1], j[1], j[1], j[1], i[10], i[10], i[10], j[3], j[3], j[3], j[3], j[2], j[2], j[2]];
        b[2] = b[2] - f[3];
        i[2] = i[2] - j[3];
        c = ["*** Start Tags", "End Tags", "Singleton Tags", "Comments", "Flat String", "String with Space at Start", "String with Space at End", "String with Space at Start and End", "SGML", "XML", "Total Parsing Declarations", "SSI", "ASP", "PHP", "Total Server Side Tags", "*** Script Tags", "*** Style Tags", "JavaScript/CSS Code"];
        z = c.length;
        for (a = 0; a < z; a += 1) {
            if (g[a] === 0) {
                h = "0.00%";
            } else if (b[a] === g[a]) {
                h = "100.00%";
            } else {
                h = ((b[a] / g[a]) * 100).toFixed(2) + "%";
            }
            if (k[a] === 0) {
                l = "0.00%";
            } else if (i[a] === k[a]) {
                l = "100.00%";
            } else {
                l = ((i[a] / k[a]) * 100).toFixed(2) + "%";
            }
            c[a] = "<tr><th>" + c[a] + "</th><td>" + b[a].toString() + "</td><td>" + h + "</td><td>" + ((b[a] / cinfo.length) * 100).toFixed(2) + "%</td><td>" + i[a] + "</td><td>" + l + "</td><td>" + ((i[a] / summary.join("").length) * 100).toFixed(2) + "%</td></tr>";
        }
        g = function (x) {
            if (f[x] === 0) {
                return "0.00%";
            } else {
                return "100.00%";
            }
        };
        k = function (x) {
            if (j[x] === 0) {
                return "0.00%";
            } else {
                return "100.00%";
            }
        };
        h = function (x) {
            var y, z;
            switch (x) {
            case 0:
                if ((f[x] / cinfo.length) < 0.7) {
                    y = "bad";
                } else {
                    y = "good";
                }
                if ((j[x] / summary.join("").length) > 0.4) {
                    z = "bad";
                } else {
                    z = "good";
                }
                break;
            case 1:
                if ((f[x] / cinfo.length) < 0.25) {
                    y = "bad";
                } else {
                    y = "good";
                }
                if ((j[x] / summary.join("").length) < 0.6) {
                    z = "bad";
                } else {
                    z = "good";
                }
                break;
            case 2:
                if ((f[x] / cinfo.length) > 0.05) {
                    y = "bad";
                } else {
                    y = "good";
                }
                if ((j[x] / summary.join("").length) > 0.05) {
                    z = "bad";
                } else {
                    z = "good";
                }
                break;
            }
            return "</th><td>" + f[x] + "</td><td>" + g(x) + "</td><td class='" + y + "'>" + ((f[x] / cinfo.length) * 100).toFixed(2) + "%</td><td>" + j[x] + "</td><td>" + k(x) + "</td><td class='" + z + "'>" + ((j[x] / summary.join("").length) * 100).toFixed(2) + "%</td></tr>";
        };
        z = m.length;
        for (a = 0; a < z; a += 1) {
            n[a] = "<li>" + m[a].replace(/\&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/\"/g, "&quot;") + "</li>";
        }
        if (n.length > 0) {
            n = "<h4>HTML elements making HTTP requests:</h4><ul>" + n.join("") + "</ul>";
        } else {
            n = "";
        }
        c.splice(18, 0, "<tr><th>Total Script and Style Tags/Code" + h(2));
        c.splice(15, 0, "<tr><th colspan='7'>Style and Script Code/Tags</th></tr>");
        c.splice(11, 0, "<tr><th colspan='7'>Server Side Tags</th></tr>");
        c.splice(8, 0, "<tr><th>Total Content" + h(1) + "<tr><th colspan='7'>Parsing Declarations</th></tr>");
        c.splice(4, 0, "<tr><th>Total Common Tags" + h(0) + "<tr><th colspan='7'>Content</th></tr>");
        c.splice(0, 0, "<div id='doc'>" + zipf() + "<table class='analysis' summary='Analysis of markup pieces.'><caption>Analysis of markup pieces.</caption><thead><tr><th>Type</th><th>Quantity of Tags</th><th>Percentage Quantity in Section</th><th>Percentage Quantity of Total</th><th>** Character Size</th><th>Percentage Size in Section</th><th>Percentage Size of Total</th></tr></thead><tbody><tr><th>Total Pieces</th><td>" + cinfo.length + "</td><td>100.00%</td><td>100.00%</td><td>" + summary.join("").length + "</td><td>100.00%</td><td>100.00%</td></tr><tr><th colspan='7'>Common Tags</th></tr>");
        c.push("</tbody></table></div><p>* The number of requests is determined from the input submitted only and does not count the additional HTTP requests supplied from dynamically executed code, frames, iframes, css, or other external entities.</p><p>** Character size is measured from the individual pieces of tags and content specifically between minification and beautification.</p><p>*** The number of starting &lt;script&gt; and &lt;style&gt; tags is subtracted from the total number of start tags. The combination of those three values from the table above should equal the number of end tags or the code is in error.</p>" + n);
        if (b[0] + b[15] + b[16] !== b[1]) {
            n = "s";
            a = (b[0] + b[15] + b[16]) - b[1];
            if (a > 0) {
                if (a === 1) {
                    n = "";
                }
                a = a + " more start tag" + n + " than end tag" + n + "! ";
            } else {
                if (a === -1) {
                    n = "";
                }
                a = (a * -1) + " more end tag" + n + " than start tag" + n + "! ";
            }
            c.splice(0, 0, "<p><em>" + a + "The combined total number of start tags, script tags, and style tags should equal the number of end tags. For HTML try the 'Presume SGML type HTML' option.</em></p>");
        }
        n = (summary.join("").length / 7500).toFixed(0);
        if (n > 0) {
            n = (m.length - n) * 4;
        } else {
            n = 0;
        }
        if (j[1] === 0) {
            f[1] = 0.00000001;
            j[1] = 0.00000001;
        }
        b = (((f[0] + f[2] - n) / cinfo.length) / (f[1] / cinfo.length));
        a = function (x, y) {
            return (((j[0] + x) / summary.join("").length) / ((j[1] * y) / summary.join("").length));
        };
        k = (b / a(j[2], 1)).toPrecision(2);
        l = (b / a(i[15], 1)).toPrecision(2);
        g = (b / a(j[2], 4)).toPrecision(2);
        h = (b / a(i[15], 4)).toPrecision(2);
        if (k === l) {
            l = "";
            h = "";
        } else {
            l = ", or <strong>" + l + "</strong> if inline script code and style tags are removed";
            h = ", or <strong>" + h + "</strong> if inline script code and style tags are removed";
        }
        c.splice(0, 0, "<p>If the input is content it receives an efficiency score of <strong>" + k + "</strong>" + l + ". The efficiency score if this input is a large form or application is <strong>" + g + "</strong>" + h + ". Efficient markup achieves scores higher than 2.00 and excellent markup achieves scores higher than 4.00. The score reflects the highest number of tags to pieces of content where the weight of those tags is as small as possible compared to the weight of the content. The score is a performance metric only and is not associated with validity or well-formedness, but semantic code typically achieves the highest scores. All values are rounded to the nearest hundreth.</p><p><strong>Total input size:</strong> <em>" + insertComma(e) + "</em> characters</p><p><strong>Total output size:</strong> <em>" + insertComma(d) + "</em> characters</p><p><strong>* Total number of HTTP requests in supplied HTML:</strong> <em>" + m.length + "</em></p>");
        return c.join('');
    };
    return build.join('').replace(/\n(\s)+\n/g, "\n\n");
};