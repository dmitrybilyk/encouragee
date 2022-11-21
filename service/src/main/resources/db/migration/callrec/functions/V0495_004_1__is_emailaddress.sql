-- CREATE FUNCTION IS_EMAILADDRESS {{{
-- returns TRUE if $1 matches the rules for RFC2822 addr-spec token,
-- ignoring CFWS in atoms, obs- versions of everything, !dot-atom
-- versions of local-part, and quoted-pairs in domain-literal (IOW,
-- this function doesn't allow backslashes after the "@"
-- FIXME: locale-dependent (relies on ranges [x-y])
/*
atext           =       ALPHA / DIGIT / ; Any character except controls,
                        "!" / "#" /     ;  SP, and specials.
                        "$" / "%" /     ;  Used for atoms
                        "&" / "'" /
                        "*" / "+" /
                        "-" / "/" /
                        "=" / "?" /
                        "^" / "_" /
                        "`" / "{" /
                        "|" / "}" /
                        "~"
dot-atom-text   =       1*atext *("." 1*atext)
dot-atom        =       [CFWS] dot-atom-text [CFWS]
addr-spec       =       local-part "@" domain
local-part      =       dot-atom / quoted-string / obs-local-part
domain          =       dot-atom / domain-literal / obs-domain
domain-literal  =       [CFWS] "[" *([FWS] dcontent) [FWS] "]" [CFWS]
dcontent        =       dtext / quoted-pair
dtext           =       NO-WS-CTL /     ; Non white space controls
                        %d33-90 /       ; The rest of the US-ASCII
                        %d94-126        ;  characters not including "[",
                                        ;  "]", or "\"
NO-WS-CTL       =       %d1-8 /         ; US-ASCII control characters
                        %d11 /          ;  that do not include the
                        %d12 /          ;  carriage return, line feed,
                        %d14-31 /       ;  and white space characters
                        %d127
*/
CREATE OR REPLACE FUNCTION callrec.IS_EMAILADDRESS(VARCHAR)
  RETURNS BOOL
  IMMUTABLE
  RETURNS NULL ON NULL INPUT
  LANGUAGE plpgsql
  AS E'
    BEGIN
      RETURN $1 ~ E''(?x) # this is an ARE
                    # local-part dot-atom-text (1*atext)
                    ^[-!#$%&''''*+/=?^_`{|}~[:alnum:]]+
                    # local-part dot-atom-text (*("." 1*atext))
                    (?:\.[-!#$%&''''*+/=?^_`{|}~[:alnum:]]+)*
                    # literal "@"
                    @
                    (?:
                      # domain (dom-atom or domain-literal)
                      #
                      # domain dot-atom (1*atext)
                      [-!#$%&''''*+/=?^_`{|}~[:alnum:]]+
                      # domain dot-atom (*("." 1*atext))
                      (?:\.[-!#$%&''''*+/=?^_`{|}~[:alnum:]]+)*
                    |
                      # domain domain-literal ("[")
                      [[]
                      # domain domain-literal (dcontent)
                      # ^@    -    ^H     ^K     ^L     ^N      ^_     "!"  -  "Z"    "^"  -  DEL
                      [\\\\x01-\\\\x08\\\\x0B\\\\x0C\\\\x0E-\\\\x1F\\\\x21-\\\\x5A\\\\x5E-\\\\x7F]*
                      # domain domain-literal ("]")
                      []]
                    )
                    $'';
    END;
  ';

GRANT EXECUTE ON FUNCTION callrec.IS_EMAILADDRESS(VARCHAR) TO GROUP callrecgrp;
-- }}}

