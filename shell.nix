{ pkgs ? import <nixpkgs> {} }:

pkgs.mkShell {
  buildInputs = with pkgs; [
    # Java for Spring Boot backend
    jdk17
    maven
    
    # Node.js for Angular frontend
    nodejs_20
    
    # Useful tools
    git
  ];

  shellHook = ''
    echo "TravelLens Development Environment"
    echo "=================================="
    echo "Java version: $(java -version 2>&1 | head -n 1)"
    echo "Node version: $(node --version)"
    echo "npm version: $(npm --version)"
    echo ""
    echo "To run the backend:  cd backend && mvn spring-boot:run"
    echo "To run the frontend: cd frontend && npm start"
    echo ""
  '';
}
