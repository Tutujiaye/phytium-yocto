require ${@oe.utils.conditional('TCMODE', 'external-arm', 'python3-anaconda.inc', '', d)}
