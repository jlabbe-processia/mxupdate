


---


# Introduction #
The MxUpdate update steps for configuration items is typically done by
removing all values and add them again. For policies this is not possible,
because otherwise there is potentially some data lost. So for policies a
"special" format is defined.


---


# Policy File Format #
The policy (excluding the properties of a policy) is defined with the TCL
procedure `updatePolicy`.

**Snippet of an Example:**
```
updatePolicy "${NAME}" {
  :
}
```

## Types ##
After the tag `type` the list of types in curly braces are defined. If
all types are defined, the tag `all` could be used instead of defining a
list.

**Snippet of an Examples with One Type:**
```
updatePolicy "${NAME}" {
  :
  type {"Test Type"}
  :
}
```
**Snippet of an Examples with All Types:**
```
updatePolicy "${NAME}" {
  :
  type all
  :
}
```

## Formats ##
After the tag `format` the list of formats in curly braces are defined. If
all formats are defined, the tag `all` could be used instead of defining a
list.

**Snippet of an example with One Format:**
```
updatePolicy "${NAME}" {
  :
  format {generic}
  :
}
```
**Snippet of an example with All Formats:**
```
updatePolicy "${NAME}" {
  :
  format all
  :
}
```

## All State Access Definition ##
The "all state access" definition is defined within `allstate`.

**Snippet of an example:**
```
updatePolicy "${NAME}" {
  :
  allstate  {
    :
    owner {read show modify}
    revoke owner {modify} filter "type==Test"
    public {none}
    user "creator" {all}
    :
  }
  :
}
```

## Owner and Public Access for States ##
The owner access is defined with the tag `owner`, the access list (in
curly braces), the tag `filter` with the filter expression. The `filter`
and expression must be only defined if wanted.

The public access uses instead the tag `public`.

For revoke access, the tag `revoke` must be define in front of the access
definitions.

**Snippet of an Example:**
```
updatePolicy "${NAME}" {
  :
  state "TestState"  {
    :
    owner {read show modify}
    revoke owner {modify} filter "type==Test"
    public {none}
    :
  }
  :
}
```


---

# Example #
```
################################################################################
# POLICY:
# ~~~~~~~
# TestPolicy
#
# SYMBOLIC NAME:
# ~~~~~~~~~~~~~~
# policy_TestPolicy
#
# DESCRIPTION:
# ~~~~~~~~~~~~
# Policy for test purposes.
#
# AUTHOR:
# ~~~~~~~
# The MxUpdate Team
################################################################################

updatePolicy "${NAME}" {
  description "Policy for test purposes."
  type {all}
  format {generic}
  defaultformat "generic"
  sequence "1,2,3,..."
  store ""
  hidden "false"
  allstate  {
    owner {}
    public {read show}
  }
  state "Submitted"  {
    registeredName "state_Submitted"
    revision "true"
    version "true"
    promote "true"
    checkouthistory "true"
    owner {read modify checkout checkin}
    public {}
    action "" input ""
    check "" input ""
    signature "Reject" {
      branch "Rejected"
      approve {Employee}
      ignore {Employee}
      reject {Employee}
      filter ""
    }
    signature "Review" {
      branch "Review"
      approve {Employee}
      ignore {Employee}
      reject {Employee}
      filter ""
    }
  }
  state "Review"  {
    registeredName "state_Review"
    revision "true"
    version "true"
    promote "true"
    checkouthistory "true"
    owner {read modify checkout}
    public {}
    action "" input ""
    check "" input ""
  }
  state "Approved"  {
    registeredName "state_Approved"
    revision "true"
    version "true"
    promote "true"
    checkouthistory "true"
    owner {read modify checkout checkin}
    public {}
    action "" input ""
    check "" input ""
    signature "creator" {
      branch ""
      approve {creator}
      ignore {}
      reject {}
      filter ""
    }
  }
  state "Rejected"  {
    revision "true"
    version "true"
    promote "true"
    checkouthistory "true"
    owner {read modify show}
    public {}
    action "" input ""
    check "" input ""
  }
}
```


---

# Limitations #
With Enovia V6R2010 it is not possible to extract filter informations for
owner / public / revoke owner / revoke public access with a XML export or with
a MQL print select statement. There exists some incidents for this behavior.
This means that a MxUpdate export does not include this filter informations.
In this case the filters must be added manually to the configuration item file.
So this also means e.g. that the standard MQL compare command does not work
correctly (because the filter informations are not exported...).

Starting with Enovia V6R2010x the extract filter information works as expected.
