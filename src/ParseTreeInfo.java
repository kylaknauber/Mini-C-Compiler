/////////////////////////////////////////////////////////////////////////////////////////
//  MIT License                                                                        //
//                                                                                     //
//  Copyright (c) 2024 Hyuntae Na                                                      //
//                                                                                     //
//  Permission is hereby granted, free of charge, to any person obtaining a copy       //
//  of this software and associated documentation files (the "Software"), to deal      //
//  in the Software without restriction, including without limitation the rights       //
//  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell          //
//  copies of the Software, and to permit persons to whom the Software is              //
//  furnished to do so, subject to the following conditions:                           //
//                                                                                     //
//  The above copyright notice and this permission notice shall be included in all     //
//  copies or substantial portions of the Software.                                    //
//                                                                                     //
//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR         //
//  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,           //
//  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE        //
//  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER             //
//  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,      //
//  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE      //
//  SOFTWARE.                                                                          //
/////////////////////////////////////////////////////////////////////////////////////////

// Name: Kyla Knauber
// Course: CMPSC 470
// Part 2 Due: April 30, 2025
// Homework 5: Semantic Checker

import java.util.ArrayList;

public class ParseTreeInfo
{
    // Use this classes to store information into parse tree node (subclasses of ParseTree.Node)
    // You should not modify ParseTree.java
    public static class TypeSpecInfo
    {
        public ParseTree.TypeSpec typespec;
        public ParseTree.LocalDecl localDecl;
        public ParseTree.Param param;
        public String typeName;
        public String id;
        public int lineno;
        public int column;
        public int address;

        public TypeSpecInfo() {
            this.typespec = getTypeSpec();
            this.typeName = getTypeName();
            this.id = getId();
            this.lineno = getLineno();
            this.column = getColumn();
            this.address = getAddress();
        }

        public void setTypeSpec(ParseTree.TypeSpec typespec) {
            this.typespec = typespec;
        }
        public ParseTree.TypeSpec getTypeSpec() {
            return typespec;
        }
        public void setLocalDecl(ParseTree.LocalDecl localDecl) {
            this.localDecl = localDecl;
        }
        public ParseTree.LocalDecl getLocalDecl() {
            return localDecl;
        }
        public void setParam(ParseTree.Param param) {
            this.param = param;
        }
        public ParseTree.Param getParam() {
            return param;
        }
        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }
        public String getTypeName() {
            return typeName;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getId() {
            return id;
        }
        public void setLineno(int lineno) {
            this.lineno = lineno;
        }
        public int getLineno() {
            return lineno;
        }
        public void setColumn(int column) {
            this.column = column;
        }
        public int getColumn() {
            return column;
        }
        public void setAddress(int address) {
            this.address = address;
        }
        public int getAddress() {
            return address;
        }

    }
    public static class ProgramInfo
    {
    }
    public static class FuncDeclInfo
    {
        public ParseTree.FuncDecl funcdecl;
        public String typename;
        public String id;
        public ArrayList<ParseTree.Param> params;
        public int lineno;
        public int column;
        public int address;
        public FuncDeclInfo() {
            this.funcdecl = getFuncDecl();
            this.typename = getTypename();
            this.id = getId();
            this.lineno = getLineno();
            this.column = getColumn();
            this.address = getAddress();
            this.params = getParams();
        }

        public void setFuncDecl(ParseTree.FuncDecl funcdecl) {
            this.funcdecl = funcdecl;
        }
        public ParseTree.FuncDecl getFuncDecl() {
            return funcdecl;
        }
        public void setTypename(String typename) {
            this.typename = typename;
        }
        public String getTypename() {
            return typename;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getId() {
            return id;
        }
        public void setLineno(int lineno) {
            this.lineno = lineno;
        }
        public int getLineno() {
            return lineno;
        }
        public void setColumn(int column) {
            this.column = column;
        }
        public int getColumn() {
            return column;
        }
        public void setAddress(int address) {
            this.address = address;
        }
        public int getAddress() {
            return address;
        }
        public void setParams(ArrayList<ParseTree.Param> params) {
            this.params = params;
        }
        public ArrayList<ParseTree.Param> getParams() {
            return this.params;
        }

    }
    public static class ParamInfo
    {
        public ParseTree.Param param;
        public String typename;
        public String id;
        public int lineno;
        public int column;
        public int address;
        public ParamInfo() {
            this.param = getParam();
            this.typename = getTypename();
            this.id = getId();
            this.lineno = getLineno();
            this.column = getColumn();
            this.address = getAddress();

        }

        public void setParam(ParseTree.Param param) {
            this.param = param;
        }
        public ParseTree.Param getParam() {
            return param;
        }
        public void setTypename(String typename) {
            this.typename = typename;
        }
        public String getTypename() {
            return typename;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getId() {
            return id;
        }
        public void setLineno(int lineno) {
            this.lineno = lineno;
        }
        public int getLineno() {
            return lineno;
        }
        public void setColumn(int column) {
            this.column = column;
        }
        public int getColumn() {
            return column;
        }
        public void setAddress(int address) {
            this.address = address;
        }
        public int getAddress() {
            return address;
        }
    }
    public static class LocalDeclInfo
    {
        public ParseTree.LocalDecl localdecl;
        public String typename;
        public String id;
        public int address;
        public int lineno;
        public int column;
        public LocalDeclInfo() {
            this.localdecl = getLocalDecl();
            this.typename = getTypename();
            this.id = getId();
        }

        public void setLocalDecl(ParseTree.LocalDecl localdecl) {
            this.localdecl = localdecl;
        }
        public ParseTree.LocalDecl getLocalDecl() {
            return localdecl;
        }
        public void setTypename(String typename) {
            this.typename = typename;
        }
        public String getTypename() {
            return typename;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getId() {
            return id;
        }
        public void setLineno(int lineno) {
            this.lineno = lineno;
        }
        public int getLineno() {
            return lineno;
        }
        public void setColumn(int column) {
            this.column = column;
        }
        public int getColumn() {
            return column;
        }
        public void setAddress(int address) {
            this.address = address;
        }
        public int getAddress() {
            return address;
        }
    }
    public static class StmtStmtInfo
    {
        public ParseTree.Stmt stmt;
        public String typename;
        public String id;
        public int address;
        public int lineno;
        public int column;
        //public ParseTree.stmtType stmtType;
        public String exprType;
        public ParseTree.Expr expr;
        public StmtStmtInfo() {
            this.stmt = getStmt();
            this.typename = getTypename();
            this.id = getId();
            this.address = getAddress();
        }

        public void setStmt(ParseTree.Stmt stmt) {
            this.stmt = stmt;
        }
        public ParseTree.Stmt getStmt() {
            return stmt;
        }
        public void setTypename(String typename) {
            this.typename = typename;
        }
        public String getTypename() {
            return typename;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getId() {
            return id;
        }
        public void setAddress(int address) {
            this.address = address;
        }
        public int getAddress() {
            return address;
        }
        public void setLineno(int lineno) {
            this.lineno = lineno;
        }
        public int getLineno() {
            return lineno;
        }
        public void setColumn(int column) {
            this.column = column;
        }
        public int getColumn() {
            return column;
        }
        public void setExprType(String exprType) {
            this.exprType = exprType;
        }
        public String getExprType() {
            return exprType;
        }
        public void setExpr(ParseTree.Expr expr) {
            this.expr = expr;
        }
        public ParseTree.Expr getExpr() {
            return expr;
        }
    }
    public static class ArgInfo
    {
        public ParseTree.Arg arg;
        public String typename;
        public String val;
        public String id;
        public int lineno;
        public int column;
        public int address;

        public ArgInfo() {
            this.arg = getArg();
            this.typename = getTypename();
            this.val = getVal();
            this.id = getId();
            this.lineno = getLineno();
            this.column = getColumn();
            this.address = getAddress();
        }

        public void setArg(ParseTree.Arg arg) {
            this.arg = arg;
        }
        public ParseTree.Arg getArg() {
            return arg;
        }
        public void setTypename(String typename) {
            this.typename = typename;
        }
        public String getTypename() {
            return typename;
        }
        public void setVal(String val) {
            this.val = val;
        }
        public String getVal() {
            return val;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getId() {
            return id;
        }
        public void setLineno(int lineno) {
            this.lineno = lineno;
        }
        public int getLineno() {
            return lineno;
        }
        public void setColumn(int column) {
            this.column = column;
        }
        public int getColumn() {
            return column;
        }
        public void setAddress(int address) {
            this.address = address;
        }
        public int getAddress() { return address; }

    }
    public static class ExprInfo extends ParseTreeInfo
    {
        public ParseTree.Expr expr;
        public String typename;
        public String val;
        public String id;
        public int lineno;
        public int column;
        public int address;
        public double valDouble;
        public ExprInfo() {
            this.expr = getExpr();
            this.typename = getTypename();
            this.val = getVal();
            this.id = getId();
            this.lineno = getLineno();
            this.column = getColumn();
            this.address = getAddress();
        }

        public void setExpr(ParseTree.Expr expr) {
            this.expr = expr;
        }
        public ParseTree.Expr getExpr() {
            return expr;
        }
        public void setTypename(String typename) {
            this.typename = typename;
        }
        public String getTypename() {
            return typename;
        }
        public void setVal(String val) {
            this.val = val;
        }
        public String getVal() {
            return val;
        }
        public void setId(String id) {
            this.id = id;
        }
        public String getId() {
            return id;
        }
        public void setLineno(int lineno) {
            this.lineno = lineno;
        }
        public int getLineno() {
            return lineno;
        }
        public void setColumn(int column) {
            this.column = column;
        }
        public int getColumn() {
            return column;
        }
        public void setAddress(int address) {
            this.address = address;
        }
        public int getAddress() { return address; }
        public void setValDouble(double valDouble) {
            this.valDouble = valDouble;
        }
        public double getValDouble() {
            return valDouble;
        }
    }

}
