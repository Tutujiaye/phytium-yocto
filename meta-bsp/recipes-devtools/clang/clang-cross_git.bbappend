require ${@oe.utils.conditional('TCMODE', 'external-arm', 'clang-cross.inc', '', d)}
