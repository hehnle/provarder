# Provarder

Provarder (Process Variant Derivation) is a tool to derive several process variants from a process model that constitutes a process family.
Provarder is based on the works: 

* an der Aalst, W.M.P., Dreiling, A., Gottschalk, F., Rosemann, M., Jansen-Vullers,
M.H.: Configurable Process Models as a Basis for Reference Modeling. In: Hutchison, D., Kanade, T., Kittler, J., Kleinberg, J.M., Mattern, F., Mitchell, J.C.,
Naor, M., Nierstrasz, O., Pandu Rangan, C., Steffen, B., Sudan, M., Terzopoulos, D., Tygar, D., Vardi, M.Y., Weikum, G., Bussler, C.J., Haller, A. (eds.)
Business Process Management Workshops, Lecture Notes in Computer Science,
vol. 3812, pp. 512–518. Springer Berlin Heidelberg, Berlin, Heidelberg (2006).
https://doi.org/10.1007/11678564 47
* Gotttschalk, F., van der Aalst, W.M.P., Jansen-Vullers, M.H., La Rosa, M.: Configurable Workflow models. International Journal of Cooperative Information Systems
17(02), 177–221 (2008). https://doi.org/10.1142/S0218843008001798
* Hongyan Zhang, Weilun Han, Chun Ouyang: Extending BPMN for Configurable
Process Modeling. In: Proceedings of the 21st ISPE Inc. International Conference on Concurrent Engineering. pp. 317–330. IOS Press (2014), https://api.
semanticscholar.org/CorpusID:22527654


## Usage
The tool is a Java command-line application that requires two inputs: the directory containing the core process model and the directory holding the configuration files. Each configuration file is a JSON file that specifies an adaptation to the process family model. The tool reads all configuration files from the provided directory and applies them to the core process model.

The configuration files must include two mandatory attributes:

* action: Can be REMOVE, REPLACE, or RESTRICT.
* BPMN element id: Identifies the BPMN element the action applies to.

The following are examples for configuration files:

```
{
  "action": "REMOVE",
  "elementId": "Activity_Notify"
}
```

```
{
  "action": "REPLACE",
  "elementId": "Event_Renewed",
  "replacementEvent": {
    "timerDuration": "P30D",
    "label": "30 days"
  }
}
```
```
{
  "action": "REPLACE",
  "elementId": "Event_Cancelled",
  "replacementEvent": {
    "messageName": "CANCEL_PARKING_PERMIT",
    "label": "parking permit cancelled"
  }
}
```
## License 
[cc-by-nc-nd]: http://creativecommons.org/licenses/by-nc-nd/4.0/


This work is licensed under a
[Creative Commons Attribution-NonCommercial-NoDerivs 4.0 International License][cc-by-nc-nd].
