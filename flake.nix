{
  description = "A very basic flake";

  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs?ref=nixos-unstable";
  };

  outputs = {
    self,
    nixpkgs,
  }: let
    systems = [
      "aarch64-linux"
      "x86_64-linux"
    ];

    forAllSystems = f:
      nixpkgs.lib.genAttrs systems (system:
        f {
          pkgs = import nixpkgs {inherit system;};
        });
  in {
    devShells = forAllSystems ({pkgs}: {
      default = pkgs.mkShell {
        packages = with pkgs; [
          maven
          zulu11
          google-java-format
          (writeShellScriptBin "format" ''
            google-java-format -r $(find . -type f -not -path '*/\.*' | sed 's/^\.\///g' | grep -E "\.java" | tr '\n' ' ')
          '')
        ];
      };
    });
  };
}
