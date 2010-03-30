# Test a number of builtin modules to verify that they can be used from 
# PythonInterpreter

import re
m = re.search('(?<=abc)def', 'abcdef')
print m.group(0)