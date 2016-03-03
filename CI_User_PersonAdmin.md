


---


# Introduction #
Administration Persons are persons in MX for which no related business
object exists (unlike ["standard" persons](CI_User_Person.md)). Good examples for
such persons are the already standard installed "guest" and "creator" account.
If the related person business object does not exists an user could not login
into the web application. This means that such persons typically used only for
administration.


---


# Comments of Persons #
Internally comments of persons are handled in the same way as descriptions of
other administration objects. Further the MQL keyword `description` works
also for person. To be near to other administration objects MxUpdate uses for
persons also `description` instead of `comment`.


---


# NOT Handled Person Properties #
The password and the password expired flag are NOT handled from MxUpdate.

## Password ##
The password could not be read from the MX database. They are hashed and only
the user itself knows the password (for better understanding see the Internet
about hashed password).

Also from security point of view the password should not be added in the CI
file, because the CI file is handled typically from a repository (and who knows
all the persons which could read the repository?).

## Password Expired Flag ##
The password expired flag is used e.g. if the user must change his password
while login. Because this is used only temporary, the flag itself is not
supported from MxUpdate (and is ignored).


---


# Steps of the Update Flow #
Following steps are done before the TCL update file is executed:
  * all access on business objects are removed
  * all access on business administration objects are removed
  * comment / description, fax number, phone number, email address, address and full name is reset (to a zero length string)
  * all assigned roles / groups are removed
  * person is set to not hidden
  * assigned default application is removed
  * assigned site is removed
  * person specific workspace objects are deleted (depends on the parameters **UserIgnoreWSO4Persons** and **UserIgnoreWS4Users**)
  * password of the person expires (depends on the parameter **UserPersonIgnorePswdNeverExpires**)
  * disables that the person wants email (depends on the parameter **UserPersonIgnoreWantsEmail**)
  * enables that the person wants icon mail (depends on the parameter **UserPersonIgnoreWantsIconMail**)


---


# Parameter Definitions #
| **Name:** `UserIgnoreWSO4Persons`            <p><b>Parameter:</b> <code>--ignorePersonWorkspaceObjects [PERSON_MATCH]</code> <code>‑‑ignoreWorkspaceObjectsForPerson [PERSON_MATCH]</code>, <code>‑‑ignorewso4person [PERSON_MATCH]</code> </p><p>Defines the match of persons for which workspace objects are not handled (neither exported nor updated).</p><p> Attention! A workspace object defined in the TCL update file are not ignored and will be created!</p> |
|:------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Name:** `UserPersonIgnoreProducts`         <p><b>Parameter:</b> <code>‑‑ignorePersonProducts [PERSON_MATCH]</code>                                                                                                         </p><p>Defines the match of persons for whom the manage of products is ignored. This means that the products for given matching person not managed anymore from the MxUpdate Update tool and must (could be) managed e.g. in separate MQL update scripts.</p> |
| **Name:** `UserPersonIgnorePswdNeverExpires` <p><b>Parameter:</b> <code>‑‑ignorePersonPasswordNeverExpires [PERSON_MATCH]</code>                                                                                             </p><p>Defines the match of persons for whom the 'password never expires' - flag is not handled from the MxUpdate Update tool. They could be managed then e.g. in separate MQL update scripts.</p>                                         |
| **Name:** `UserPersonIgnoreWantsEmail`       <p><b>Parameter:</b> <code>‑‑ignorePersonWantsEmail [PERSON_MATCH]</code>                                                                                                       </p><p>Defines the match of persons for whom the 'wants email' - flag is not handled from the MxUpdate Update tool. They could be managed then e.g. in separate MQL update scripts.</p>                                                    |
| **Name:** `UserPersonIgnoreWantsIconMail`    <p><b>Parameter:</b> <code>‑‑ignorePersonWantsIconMail [PERSON_MATCH]</code>                                                                                                    </p><p>Defines the match of persons for whom the 'wants icon mail' - flag is not handled from the MxUpdate Update tool. They could be managed then e.g. in separate MQL update scripts.</p>                                                |


---


# Example #
```
################################################################################
# PERSONADMIN:
# ~~~~~~~~~~~~
# MxUpdate_Person
#
# SYMBOLIC NAME:
# ~~~~~~~~~~~~~~
# person_MxUpdate_Person
#
# DESCRIPTION:
# ~~~~~~~~~~~~
# Administration person for test purposes.
#
# AUTHOR:
# ~~~~~~~
# The MxUpdate Team
################################################################################

mql escape mod person "${NAME}" \
    description "Administration person for test purposes." \
    !hidden \
    !neverexpires \
    access "all" \
    admin "none" \
    disable email \
    enable iconmail \
    address "" \
    email "" \
    fax "" \
    fullname "" \
    phone "" \
    vault ""
mql mod person "${NAME}" type application,full,notbusiness,notinactive,nottrusted,notsystem
setProducts "CPF"
```