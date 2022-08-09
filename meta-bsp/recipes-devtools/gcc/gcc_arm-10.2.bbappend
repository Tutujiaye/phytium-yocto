require ${@oe.utils.conditional('TCMODE', 'external-arm', 'gcc_arm-10.2.inc', '', d)}
