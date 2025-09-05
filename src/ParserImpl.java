// Name: Kyla Knauber
// Course: CMPSC 470
// Part 2 Due: April 30, 2025
// Homework 5: Semantic Checker

import java.util.*;
import java.util.HashMap;

@SuppressWarnings("unchecked")
public class ParserImpl
{
    public static Boolean _debug = true;
    void Debug(String message)
    {
        if(_debug)
            System.out.println(message);
    }
    // This is for chained symbol table.
    // This includes the global scope only at this moment.
    Env env = new Env(null);

    HashMap<String, String> funcReturnTypes = new HashMap<>();

    int localDeclRelAddrCounter = 0;
    int paramRelAddrCounter = 0;

    // this stores the root of parse tree, which will be used to print parse tree and run the parse tree
    ParseTree.Program parsetree_program = null;

    Object program____decllist(Object s1) throws Exception {
        // 1. check if decllist has main function having no parameters and returns int type
        // 2. assign the root, whose type is ParseTree.Program, to parsetree_program
        ArrayList<ParseTree.FuncDecl> decllist = (ArrayList<ParseTree.FuncDecl>)s1;
        if(decllist.isEmpty()) {
            throw new Exception("The program must have one main function that returns num value and has no parameters.");
        }
        for(int i = 0; i < decllist.size(); i++) {
            ParseTree.FuncDecl decl = decllist.get(i);
            if(decl.ident.equals("main")) {
                if(!decl.params.isEmpty()) {
                    throw new Exception("The program must have one main function that returns num value and has no parameters.");
                }
                if(!decl.rettype.typename.equals("num")) {
                    throw new Exception("The program must have one main function that returns num value and has no parameters.");
                }
            }
        }
        parsetree_program = new ParseTree.Program(decllist);
        return parsetree_program;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    Object decllist____decllist_decl(Object s1, Object s2) throws Exception {
        ArrayList<ParseTree.FuncDecl> decllist = (ArrayList<ParseTree.FuncDecl>)s1;
        ParseTree.FuncDecl                decl = (ParseTree.FuncDecl           )s2;
        decllist.add(decl);
        return decllist;
    }
    Object decllist____eps() throws Exception {
        return new ArrayList<ParseTree.FuncDecl>();
    }
    Object decl____funcdecl(Object s1) throws Exception {
        return s1;
    }
    Object primtype____NUM(Object s1) throws Exception {
        ParseTree.TypeSpec numKeyWord = new ParseTree.TypeSpec("num");
        return numKeyWord;
    }
    Object primtype____BOOL(Object s1) throws Exception {
        ParseTree.TypeSpec boolKeyword = new ParseTree.TypeSpec("bool");
        return boolKeyword;
    }
    Object typespec____primtype(Object s1) {
        ParseTree.TypeSpec primtype = (ParseTree.TypeSpec)s1;
        return primtype;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    Object fundecl____typespec_ID_LPAREN_params_RPAREN_BEGIN_localdecls_8X_stmtlist_END(Object s1, Object s2, Object s3, Object s4, Object s5, Object s6, Object s7) throws Exception {
        // 1. add function_type_info object (name, return type, params) into the global scope of env
        // 2. create a new symbol table on top of env
        // 3. add parameters into top-local scope of env
        // 4. etc.
        ParseTree.TypeSpec               rettype    = (ParseTree.TypeSpec              )s1;
        Token                            id         = (Token                           )s2;
        Token                            lparen     = (Token                           )s3;
        ArrayList<ParseTree.Param>       params     = (ArrayList<ParseTree.Param>      )s4;
        Token                            rparen     = (Token                           )s5;
        Token                            begin      = (Token                           )s6;
        ArrayList<ParseTree.LocalDecl>   localdecls = (ArrayList<ParseTree.LocalDecl>  )s7;

        // List to store funcInfo in global scope
        ArrayList<Object> funcInfo = new ArrayList<>();
        funcInfo.add(id.lexeme); // may not need this
        funcInfo.add(rettype.typename);
        funcInfo.add(params);
        funcInfo.add("funcdecl");

        // Create a new symbol table for the local scope of func
        Env newEnv;

        // If this.env.prev is null, then this is the first function
        if(this.env.prev == null) {
            // Using this as new global scope
            newEnv = new Env(null);
            // Putting funcDecl info in the global scope
            this.env.Put(id.lexeme, funcInfo);
            newEnv = this.env;
            Env temp = new Env(newEnv);
            // this.env will always be the current local scope
            this.env = temp;
        }
        else {
            newEnv = new Env(this.env.prev);
            newEnv = this.env;
            Env temp = new Env(newEnv);
            this.env = temp;
            Env current = this.env;
            while(current != null) {
                current = current.prev;
                if(current.prev == null) {
                    // Putting funcDecl info in the global scope
                    current.Put(id.lexeme, funcInfo);
                    break;
                }
            }
        }
        funcReturnTypes.put(id.lexeme, rettype.typename);

        for(int i = 0; i < params.size(); i++){
            ParseTree.TypeSpec idType = (ParseTree.TypeSpec)env.Get(params.get(i).ident);
            if(env.Get(params.get(i).ident) != null && params.get(i).typespec.typename.equals(idType.typename)) {
                throw new Exception("[Error at " + params.get(i).info.lineno + ":" + params.get(i).info.column + "] Identifier " + params.get(i).ident + " is already defined.");
            }
            env.Put(params.get(i).ident, params.get(i).typespec);
        }
        for(int i = 0; i < localdecls.size(); i++){
            ParseTree.TypeSpec idType = (ParseTree.TypeSpec)env.Get(localdecls.get(i).ident);
            if(env.Get(localdecls.get(i).ident) != null && localdecls.get(i).typespec.typename.equals(idType.typename)) {
                throw new Exception("[Error at " + localdecls.get(i).info.lineno + ":" + localdecls.get(i).info.column + "] Identifier " + localdecls.get(i).ident + " is already defined.");
            }
            env.Put(localdecls.get(i).ident, localdecls.get(i).typespec);
        }

        ParseTreeInfo.FuncDeclInfo funcDeclInfo = new ParseTreeInfo.FuncDeclInfo();
        funcDeclInfo.setFuncDecl(new ParseTree.FuncDecl(id.lexeme, rettype, params, localdecls, null));
        funcDeclInfo.setTypename(rettype.typename);
        funcDeclInfo.setId(id.lexeme);
        funcDeclInfo.setColumn(id.column);
        funcDeclInfo.setLineno(id.lineno);
        funcDeclInfo.setParams(params);

        return null;
    }
    Object fundecl____typespec_ID_LPAREN_params_RPAREN_BEGIN_localdecls_X8_stmtlist_END(Object s1, Object s2, Object s3, Object s4, Object s5, Object s6, Object s7, Object s8, Object s9, Object s10) throws Exception {
        // 1. check if this function has at least one return type
        // 2. etc.
        // 3. create and return funcdecl node
        ParseTree.TypeSpec               rettype    = (ParseTree.TypeSpec              )s1;
        Token                            id         = (Token                           )s2;
        Token lparen = (Token)s3;
        ArrayList<ParseTree.Param>       params     = (ArrayList<ParseTree.Param>      )s4;
        Token rparen = (Token)s5;
        ArrayList<ParseTree.LocalDecl>   localdecls = (ArrayList<ParseTree.LocalDecl>  )s7;
        ArrayList<ParseTree.Stmt>        stmtlist   = (ArrayList<ParseTree.Stmt>       )s9;
        Token                            end        = (Token                           )s10;
        localDeclRelAddrCounter = 0;
        paramRelAddrCounter = 0;
        for(int i = 0; i < stmtlist.size(); i++){
            if(stmtlist.get(i).info.id.equals("return")) {
                String returnExprType = stmtlist.get(i).info.expr.info.typename;
                if(returnExprType.equals(rettype.typename)) {
                    this.env = this.env.prev;
                    ParseTree.FuncDecl funcdecl = new ParseTree.FuncDecl(id.lexeme, rettype, params, localdecls, stmtlist);
                    return funcdecl;
                }
                else {
                    throw new Exception("[Error at " + stmtlist.get(i).info.expr.info.lineno + ":" + stmtlist.get(i).info.expr.info.column + "] Function " + id.lexeme + lparen.lexeme + rparen.lexeme + " should return a " + rettype.typename + " value, instead of a " + stmtlist.get(i).info.expr.info.typename + " value.");
                }
            }
            if(stmtlist.get(i).info.id.equals("if")) {
                ParseTree.IfStmt ifStmt = (ParseTree.IfStmt)stmtlist.get(i);
                if(ifStmt.elsestmt.info.id.equals("return")) {
                    String returnExprType = ifStmt.elsestmt.info.expr.info.typename;
                    if(returnExprType.equals(rettype.typename)) {
                        this.env = this.env.prev;
                        ParseTree.FuncDecl funcdecl = new ParseTree.FuncDecl(id.lexeme, rettype, params, localdecls, stmtlist);
                        return funcdecl;
                    }
                    else {
                        throw new Exception("[Error at " + ifStmt.elsestmt.info.lineno + ":" + ifStmt.elsestmt.info.column + "] Function " + id.lexeme + lparen.lexeme + rparen.lexeme + " should return a " + rettype.typename + " value, instead of a " + ifStmt.elsestmt.info.expr.info.typename + " value.");
                    }
                }
                else if(ifStmt.thenstmt.info.id.equals("return")) {
                    String returnExprType = ifStmt.thenstmt.info.expr.info.typename;
                    if(returnExprType.equals(rettype.typename)) {
                        this.env = this.env.prev;
                        ParseTree.FuncDecl funcdecl = new ParseTree.FuncDecl(id.lexeme, rettype, params, localdecls, stmtlist);
                        return funcdecl;
                    }
                    else {
                        throw new Exception("[Error at " + ifStmt.thenstmt.info.lineno + ":" + ifStmt.thenstmt.info.column + "] Function " + id.lexeme + lparen.lexeme + rparen.lexeme + " should return a " + rettype.typename + " value, instead of a " + ifStmt.thenstmt.info.expr.info.typename + " value.");
                    }
                }
                else if(ifStmt.elsestmt.info.typename.equals("compoundstmt")) {
                    ParseTree.CompoundStmt compoundStmt = (ParseTree.CompoundStmt)ifStmt.elsestmt;
                    for(int j = 0; j < compoundStmt.stmtlist.size(); j++) {
                        if(compoundStmt.stmtlist.get(j).info.typename.equals("return")) {
                            String returnExprType = compoundStmt.stmtlist.get(j).info.exprType;
                            if(returnExprType.equals(rettype.typename)) {
                                this.env = this.env.prev;
                                ParseTree.FuncDecl funcdecl = new ParseTree.FuncDecl(id.lexeme, rettype, params, localdecls, stmtlist);
                                return funcdecl;
                            }
                            else {
                                throw new Exception("[Error at " + compoundStmt.stmtlist.get(j).info.lineno + ":" + compoundStmt.stmtlist.get(j).info.column + "] Function " + id.lexeme + lparen.lexeme + rparen.lexeme + " should return a " + rettype.typename + " value, instead of a " + compoundStmt.stmtlist.get(j).info.exprType+ " value.");
                            }
                        }
                    }
                }
                else if(ifStmt.thenstmt.info.typename.equals("compoundstmt")) {
                    ParseTree.CompoundStmt compoundStmt = (ParseTree.CompoundStmt)ifStmt.thenstmt;
                    for(int j = 0; j < compoundStmt.stmtlist.size(); j++) {
                        if(compoundStmt.stmtlist.get(j).info.typename.equals("return")) {
                            String returnExprType = compoundStmt.stmtlist.get(j).info.exprType;
                            if(returnExprType.equals(rettype.typename)) {
                                this.env = this.env.prev;
                                ParseTree.FuncDecl funcdecl = new ParseTree.FuncDecl(id.lexeme, rettype, params, localdecls, stmtlist);
                                return funcdecl;
                            }
                            else {
                                throw new Exception("[Error at " + compoundStmt.stmtlist.get(j).info.lineno + ":" + compoundStmt.stmtlist.get(j).info.column + "] Function " + id.lexeme + lparen.lexeme + rparen.lexeme + " should return a " + rettype.typename + " value, instead of a " + compoundStmt.stmtlist.get(j).info.exprType + " value.");
                            }
                        }
                    }
                }
            }
            if(stmtlist.get(i).info.id.equals("while")) {
                ParseTree.WhileStmt whileStmt = (ParseTree.WhileStmt)stmtlist.get(i);
                if(whileStmt.stmt.info.id.equals("return")) {
                    String returnExprType = whileStmt.stmt.info.expr.info.typename;
                    if(returnExprType.equals(rettype.typename)) {
                        this.env = this.env.prev;
                        ParseTree.FuncDecl funcdecl = new ParseTree.FuncDecl(id.lexeme, rettype, params, localdecls, stmtlist);
                        return funcdecl;
                    }
                    else {
                        throw new Exception("[Error at " + whileStmt.stmt.info.lineno + ":" + whileStmt.stmt.info.column + "] Function " + id.lexeme + lparen.lexeme + rparen.lexeme + " should return a " + rettype.typename + " value, instead of a " + whileStmt.stmt.info.expr.info.typename + " value.");
                    }
                }
                if(whileStmt.info.typename.equals("compoundstmt")) {
                    ParseTree.CompoundStmt compoundStmt = (ParseTree.CompoundStmt) whileStmt.stmt;
                    for(int j = 0; j < compoundStmt.stmtlist.size(); j++) {
                        if(compoundStmt.stmtlist.get(j).info.typename.equals("return")) {
                            String returnExprType = compoundStmt.stmtlist.get(j).info.exprType;
                            if(returnExprType.equals(rettype.typename)) {
                                this.env = this.env.prev;
                                ParseTree.FuncDecl funcdecl = new ParseTree.FuncDecl(id.lexeme, rettype, params, localdecls, stmtlist);
                                return funcdecl;
                            }
                            else {
                                throw new Exception("[Error at " + compoundStmt.stmtlist.get(j).info.lineno + ":" + compoundStmt.stmtlist.get(j).info.column + "] Function " + id.lexeme + lparen.lexeme + rparen.lexeme + " should return a " + rettype.typename + " value, instead of a " + compoundStmt.stmtlist.get(j).info.exprType + " value.");
                            }
                        }
                    }
                }

            }
            if(stmtlist.get(i).info.id.equals("compoundstmt")) {
                ParseTree.CompoundStmt compoundStmt = (ParseTree.CompoundStmt)stmtlist.get(i);
                for(int j = 0; j < compoundStmt.stmtlist.size(); j++) {
                    if(compoundStmt.stmtlist.get(j).info.typename.equals("return")) {
                        String returnExprType = compoundStmt.stmtlist.get(j).info.expr.info.typename;
                        if(returnExprType.equals(rettype.typename)) {
                            this.env = this.env.prev;
                            ParseTree.FuncDecl funcdecl = new ParseTree.FuncDecl(id.lexeme, rettype, params, localdecls, stmtlist);
                            return funcdecl;
                        }
                        else {
                            throw new Exception("[Error at " + compoundStmt.stmtlist.get(j).info.lineno + ":" + compoundStmt.stmtlist.get(j).info.column + "] Function " + id.lexeme + lparen.lexeme + rparen.lexeme + " should return a " + rettype.typename + " value, instead of a " + compoundStmt.stmtlist.get(j).info.expr.info.typename + " value.");
                        }
                    }
                }
            }
        }
        this.env = this.env.prev;
        throw new Exception("[Error at " + end.lineno + ":" + end.column + "] Function " + id.lexeme + lparen.lexeme + rparen.lexeme + " should return at least one value.");
    }

    Object params____paramlist(Object s1) throws Exception {
        ArrayList<ParseTree.Param> paramlist = (ArrayList<ParseTree.Param>)s1;
        return paramlist;
    }
    Object params____eps() throws Exception {
        return new ArrayList<ParseTree.Param>();
    }
    Object paramlist____paramlist_COMMA_param(Object s1, Object s2, Object s3) throws Exception {
        ArrayList<ParseTree.Param> paramlist = (ArrayList<ParseTree.Param>)s1;
        Token                      comma     = (Token                     )s2;
        ParseTree.Param            param     = (ParseTree.Param           )s3;
        paramlist.add(param);
        return paramlist;
    }
    Object paramlist____param(Object s1) throws Exception {
        ParseTree.Param param = (ParseTree.Param)s1;
        ArrayList<ParseTree.Param> paramlist = new ArrayList<ParseTree.Param>();
        paramlist.add(param);
        return paramlist;
    }
    Object param____typespec_IDENT(Object s1, Object s2) throws Exception {
        ParseTree.TypeSpec typespec = (ParseTree.TypeSpec)s1;
        Token              id       = (Token             )s2;
//        if(this.env.Get(id.lexeme) != null) {
//            throw new Exception("Identifier " + id.lexeme + " is already defined.");
//        }
        ParseTree.Param param = new ParseTree.Param(id.lexeme, typespec);
        ParseTreeInfo.ParamInfo paramInfo = new ParseTreeInfo.ParamInfo();
        ParseTreeInfo.TypeSpecInfo typeInfo = new ParseTreeInfo.TypeSpecInfo();
        paramInfo.setParam(param);
        paramInfo.setTypename(typespec.typename);
        paramInfo.setId(id.lexeme);
        paramInfo.setColumn(id.column);
        paramInfo.setLineno(id.lineno);
        typeInfo.setTypeSpec(typespec);
        typeInfo.setTypeName(typespec.typename);
        typeInfo.setId(id.lexeme);
        typeInfo.setColumn(id.column);
        typeInfo.setLineno(id.lineno);
        typeInfo.setParam(param);


        paramRelAddrCounter--;
        param.reladdr = paramRelAddrCounter;
        paramInfo.setAddress(paramRelAddrCounter);
        typeInfo.setAddress(paramRelAddrCounter);
        typespec.info = typeInfo;
        param.info = paramInfo;
        return param;
    }
    Object typespec____primtype_LBRACKET_RBRACKET(Object s1, Object s2, Object s3) throws Exception {
        ParseTree.TypeSpec primtype = (ParseTree.TypeSpec)s1;
        Token              lbracket = (Token             )s2;
        Token              rbracket = (Token             )s3;
        ParseTree.TypeSpec primTypeArr = new ParseTree.TypeSpec(primtype.typename + "[]");
        //this.env.Put(primtype.typename, primTypeArr);
        ParseTreeInfo.TypeSpecInfo typeInfo = new ParseTreeInfo.TypeSpecInfo();
        typeInfo.setTypeSpec(primTypeArr);
        typeInfo.setTypeName(primtype.typename);
        typeInfo.setId(primtype.typename+ "[]");
        //typeInfo.setColumn(primtype.typename.co);
        primTypeArr.info = typeInfo;
        return primTypeArr;
    }

    Object stmtlist____stmtlist_stmt(Object s1, Object s2) throws Exception {
        ArrayList<ParseTree.Stmt> stmtlist = (ArrayList<ParseTree.Stmt>)s1;
        ParseTree.Stmt            stmt     = (ParseTree.Stmt           )s2;
        stmtlist.add(stmt);
        return stmtlist;
    }
    Object stmtlist____eps() throws Exception {
        return new ArrayList<ParseTree.Stmt>();
    }
    Object stmt____assignstmt  (Object s1) throws Exception {
        assert(s1 instanceof ParseTree.AssignStmt);
        return s1;
    }
    Object stmt____returnstmt  (Object s1) throws Exception {
        assert(s1 instanceof ParseTree.ReturnStmt);
        return s1;
    }
    Object stmt____printstmt   (Object s1) throws Exception {
        assert(s1 instanceof ParseTree.PrintStmt);
        return s1;
    }
    Object stmt____ifstmt      (Object s1) throws Exception {
        assert(s1 instanceof ParseTree.IfStmt);
        return s1;
    }
    Object stmt____whilestmt(Object s1) {
        assert(s1 instanceof ParseTree.WhileStmt);
        return s1;
    }
    Object stmt____compoundstmt(Object s1) {
        assert(s1 instanceof ParseTree.CompoundStmt);
        return s1;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    Object assignstmt____IDENT_ASSIGN_expr_SEMI(Object s1, Object s2, Object s3, Object s4) throws Exception {
        // 1. check if ident.value_type matches with expr.value_type
        // 2. etc.
        // e. create and return node
        Token          id     = (Token         )s1;
        Token          assign = (Token         )s2;
        ParseTree.Expr expr   = (ParseTree.Expr)s3;
        ParseTree.TypeSpec id_type = (ParseTree.TypeSpec) env.Get(id.lexeme);
        String idTypename = id_type.typename;
        if(id_type == null) {
            throw new Exception("[Error at " + id.lineno + ":" + id.column + "] Identifier " + id.lexeme + " is not defined.");
        }
        if(id_type.typename.contains("[]")) {
            idTypename = id_type.typename.replace("[]", "");
        }

        if(idTypename.equals(expr.info.typename)) {
            // Okay
        }
        else {
            throw new Exception("[Error at " + id.lineno + ":" + id.column + "] Variable " + id.lexeme + " should have " + id_type.typename + " value, instead of " + expr.info.typename + " value.");
        }

        ParseTreeInfo.StmtStmtInfo stmtInfo = new ParseTreeInfo.StmtStmtInfo();
        stmtInfo.setTypename("assignstmt");
        stmtInfo.setId("assign");

        ParseTree.AssignStmt stmt = new ParseTree.AssignStmt(id.lexeme, expr);
        stmt.ident_reladdr = ((ParseTree.TypeSpec) env.Get(id.lexeme)).info.address;
        stmtInfo.address = stmt.ident_reladdr;
        stmt.info = stmtInfo;
        return stmt;
    }
    Object assignstmt____IDENT_LBRACKET_expr_RBRACKET_ASSIGN_expr_SEMI(Object s1, Object s2, Object s3, Object s4, Object s5, Object s6, Object s7) throws Exception{
        Token          id     = (Token         )s1;
        Token          lbracket = (Token         )s2;
        ParseTree.Expr exprInBrackets   = (ParseTree.Expr)s3;
        Token          rbracket = (Token         )s4;
        Token assign = (Token)s5;
        ParseTree.Expr expr2 = (ParseTree.Expr)s6;
        Token semi =  (Token)s7;
        ParseTree.TypeSpec id_type = (ParseTree.TypeSpec) env.Get(id.lexeme);
        if(id_type == null) {
            throw new Exception("[Error at " + id.lineno + ":" + id.column + "] Identifier " + id.lexeme + " is not defined.");
        }
        String idTypename = id_type.typename;
        if(id_type.typename.contains("[]")) {
            idTypename = id_type.typename.replace("[]", "");
        }
        if(!exprInBrackets.info.typename.equals("num")) {
            throw new Exception("[Error at " + exprInBrackets.info.lineno + ":" + exprInBrackets.info.column + "] Array index must be num value.");
        }
        if(!expr2.info.typename.equals(idTypename)) {
            throw new Exception("[Error at " + id.lineno + ":" + id.column + "] Element of array " + id.lexeme + " should have a " + idTypename + " value, instead of a " + expr2.info.typename + " value.");
        }

        ParseTree.AssignStmtForArray arrayStmt = new ParseTree.AssignStmtForArray(id.lexeme, exprInBrackets, expr2);
        arrayStmt.ident_reladdr = ((ParseTree.TypeSpec) env.Get(id.lexeme)).info.address;
        ParseTreeInfo.StmtStmtInfo stmtInfo = new ParseTreeInfo.StmtStmtInfo();
        stmtInfo.setTypename("assignstmt");
        stmtInfo.setId("assign");
        stmtInfo.setColumn(id.column);
        stmtInfo.setLineno(id.lineno);
        stmtInfo.address = arrayStmt.ident_reladdr;
        arrayStmt.info = stmtInfo;
        return arrayStmt;
    }
    Object returnstmt____RETURN_expr_SEMI(Object s1, Object s2, Object s3) throws Exception {
        // 1. check if expr.value_type matches with the current function return type
        // 2. etc.
        // 3. create and return node
        Token returnKeyword = (Token)s1;
        ParseTree.Expr expr = (ParseTree.Expr)s2;
        String exprType = expr.info.typename;
        ParseTreeInfo.StmtStmtInfo stmtInfo = new ParseTreeInfo.StmtStmtInfo();
        stmtInfo.setTypename("return");
        stmtInfo.setColumn(returnKeyword.column);
        stmtInfo.setLineno(returnKeyword.lineno);
        stmtInfo.setId(returnKeyword.lexeme);
        stmtInfo.setExprType(exprType);
        stmtInfo.setExpr(expr);
        ParseTree.ReturnStmt returnStmt = new ParseTree.ReturnStmt(expr);
        returnStmt.info = stmtInfo;
        //String funcReturnType = funcReturnTypes.get("main");

        return returnStmt;
    }
    Object printstmt____PRINT_expr_SEMI(Object s1, Object s2, Object s3) throws Exception {
        Token          printKeyword     = (Token         )s1;
        ParseTree.Expr expr = (ParseTree.Expr)s2;
        Token printToken = (Token)s3;

        ParseTreeInfo.StmtStmtInfo stmtInfo = new ParseTreeInfo.StmtStmtInfo();
        stmtInfo.setTypename("printstmt");
        stmtInfo.setId(printKeyword.lexeme);
        stmtInfo.setColumn(printKeyword.column);
        stmtInfo.setLineno(printKeyword.lineno);
        ParseTree.PrintStmt printStmt = new ParseTree.PrintStmt(expr);
        printStmt.info = stmtInfo;

        return printStmt;
    }
    Object ifstmt____IF_LPAREN_expr_RPAREN_stmt_ELSE_stmt(Object s1, Object s2, Object s3, Object s4, Object s5, Object s6, Object s7) throws Exception {
        Token          ifKeyword     = (Token           )s1;
        Token lparen = (Token)s2;
        ParseTree.Expr condExpr = (ParseTree.Expr)s3;
        Token rparen = (Token)s4;
        ParseTree.Stmt stmt1 = (ParseTree.Stmt)s5;
        Token elseKeyword = (Token)s6;
        ParseTree.Stmt stmt2 = (ParseTree.Stmt)s7;
        if(!condExpr.info.typename.equals("bool")) {
            throw new Exception("[Error at " + condExpr.info.lineno + ":" + condExpr.info.column + "] Condition of if or while statement should be a bool value.");
        }
        ParseTreeInfo.StmtStmtInfo stmtInfo = new ParseTreeInfo.StmtStmtInfo();
        ParseTree.IfStmt ifStmt = new ParseTree.IfStmt(condExpr, stmt1, stmt2);
        stmtInfo.setTypename("ifstmt");
        stmtInfo.setId(ifKeyword.lexeme);
        stmtInfo.setColumn(ifKeyword.column);
        stmtInfo.setLineno(ifKeyword.lineno);
        stmtInfo.stmt = ifStmt;
        ifStmt.info = stmtInfo;

        return ifStmt;
    }
    Object whilestmt____WHILE_LPAREN_expr_RPAREN_stmt(Object s1, Object s2, Object s3, Object s4, Object s5) throws Exception {
        Token whileKeyword = (Token)s1;
        Token lparen = (Token)s2;
        ParseTree.Expr condExpr = (ParseTree.Expr)s3;
        Token rparen = (Token)s4;
        ParseTree.Stmt stmt1 = (ParseTree.Stmt)s5;
        if(!condExpr.info.typename.equals("bool")) {
            throw new Exception("[Error at " + condExpr.info.lineno + ":" + condExpr.info.column + "] Condition of if or while statement should be a bool value.");
        }
        ParseTreeInfo.StmtStmtInfo stmtInfo = new ParseTreeInfo.StmtStmtInfo();
        ParseTree.WhileStmt whileStmt = new ParseTree.WhileStmt(condExpr, stmt1);
        stmtInfo.setTypename("whilestmt");
        stmtInfo.setId(whileKeyword.lexeme);
        stmtInfo.setColumn(whileKeyword.column);
        stmtInfo.setLineno(whileKeyword.lineno);
        // set address
        whileStmt.info = stmtInfo;

        return whileStmt;
    }
    Object compoundstmt____BEGIN_localdecls(Object s1, Object s2) throws Exception {
        Token begin = (Token)s1;
        ArrayList<ParseTree.LocalDecl> localDecls = (ArrayList<ParseTree.LocalDecl>)s2;
        Env newEnv;
        if(this.env.prev == null) {
            newEnv = new Env(null);
            newEnv = this.env;
            Env temp = new Env(newEnv);
            this.env = temp;
        }
        else {
            newEnv = new Env(this.env.prev);
            newEnv = this.env;
            Env temp = new Env(newEnv);
            this.env = temp;
        }
        this.env.prev = newEnv;
        for(int i = 0; i < localDecls.size(); i++) {
            ParseTree.LocalDecl localDecl = localDecls.get(i);
            ParseTree.TypeSpec idType = (ParseTree.TypeSpec)env.Get(localDecl.ident);
            if(env.Get(localDecl.ident) != null && localDecl.typespec.typename.equals(idType.typename)) {
                throw new Exception("[Error at " + localDecl.info.lineno + ":" + localDecl.info.column + "] Identifier " + localDecl.ident + " is already defined.");
            }
            env.Put(localDecl.ident, localDecl.typespec);
        }

        return null;
    }
    Object compoundstmt____BEGIN_localdecls_stmtlist_END(Object s1, Object s2, Object s3, Object s4, Object s5) throws Exception {
        Token begin = (Token)s1;
        ArrayList<ParseTree.LocalDecl> localdeclsList = (ArrayList<ParseTree.LocalDecl>)s2;
        ArrayList<ParseTree.Stmt> stmtList = (ArrayList<ParseTree.Stmt>)s4;
        Token end = (Token)s5;

        // Remove top local scope
        this.env = this.env.prev;

        ParseTreeInfo.StmtStmtInfo stmtInfo = new ParseTreeInfo.StmtStmtInfo();
        stmtInfo.setTypename("compoundstmt");
        stmtInfo.setId(begin.lexeme);
        stmtInfo.setColumn(begin.column);
        stmtInfo.setLineno(begin.lineno);
        ParseTree.CompoundStmt compoundStmt = new ParseTree.CompoundStmt(localdeclsList, stmtList);
        stmtInfo.setStmt(compoundStmt);
        compoundStmt.info = stmtInfo;

        return compoundStmt;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    Object localdecls____localdecls_localdecl(Object s1, Object s2) {
        ArrayList<ParseTree.LocalDecl> localdecls = (ArrayList<ParseTree.LocalDecl>)s1;
        ParseTree.LocalDecl            localdecl  = (ParseTree.LocalDecl           )s2;
        localdecls.add(localdecl);
        return localdecls;
    }
    Object localdecls____eps() throws Exception {
        return new ArrayList<ParseTree.LocalDecl>();
    }
    Object localdecl____typespec_IDENT_SEMI(Object s1, Object s2, Object s3) throws Exception {
        ParseTree.TypeSpec typespec = (ParseTree.TypeSpec)s1;
        Token              id       = (Token             )s2;
        ParseTree.LocalDecl localdecl = new ParseTree.LocalDecl(id.lexeme, typespec);
        ParseTree.TypeSpec idType = (ParseTree.TypeSpec)env.Get(id.lexeme);

        localDeclRelAddrCounter++;
        localdecl.reladdr = localDeclRelAddrCounter;

        ParseTreeInfo.LocalDeclInfo localDeclInfo = new ParseTreeInfo.LocalDeclInfo();
        ParseTreeInfo.TypeSpecInfo typeSpecInfo = new ParseTreeInfo.TypeSpecInfo();
        localDeclInfo.setLocalDecl(localdecl);
        localDeclInfo.setTypename(typespec.typename);
        localDeclInfo.setId(id.lexeme);
        localDeclInfo.setColumn(id.column);
        localDeclInfo.setLineno(id.lineno);
        localDeclInfo.setAddress(localDeclRelAddrCounter);
        typeSpecInfo.setTypeSpec(typespec);
        typeSpecInfo.setTypeName(typespec.typename);
        typeSpecInfo.setId(id.lexeme);
        typeSpecInfo.setColumn(id.column);
        typeSpecInfo.setLineno(id.lineno);
        typeSpecInfo.setAddress(localDeclRelAddrCounter);
        typespec.info = typeSpecInfo;
        localdecl.info = localDeclInfo;

        return localdecl;
    }
    Object args____arglist(Object s1) throws Exception {
        ArrayList<ParseTree.Arg> argList = (ArrayList<ParseTree.Arg>)s1;
        return argList;
    }
    Object args____eps() throws Exception {
        return new ArrayList<ParseTree.Expr>();
    }
    Object arglist____expr(Object s1) throws Exception {
        ParseTree.Expr expr = (ParseTree.Expr)s1;
        ArrayList<ParseTree.Arg> argList = new ArrayList<>();
        ParseTree.Arg args = new ParseTree.Arg(expr);
        ParseTreeInfo.ArgInfo argInfo = new ParseTreeInfo.ArgInfo();
        argInfo.setArg(args);
        argInfo.setTypename(expr.info.typename);
        argInfo.setId(expr.info.id);
        argInfo.setColumn(expr.info.column);
        argInfo.setLineno(expr.info.lineno);
        argInfo.setAddress(expr.info.address);
        args.info = argInfo;
        argList.add(args);
        return argList;
    }
    Object arglist____arglist_COMMA_expr(Object s1, Object s2, Object s3) throws Exception {
        ArrayList<ParseTree.Arg> argList = (ArrayList<ParseTree.Arg>)s1;
        Token                     comma    = (Token                     )s2;
        ParseTree.Expr            expr     = (ParseTree.Expr            )s3;

        ParseTree.Arg args = new ParseTree.Arg(expr);
        ParseTreeInfo.ArgInfo argInfo = new ParseTreeInfo.ArgInfo();
        argInfo.setArg(args);
        argInfo.setTypename(expr.info.typename);
        argInfo.setId(expr.info.id);
        argInfo.setColumn(expr.info.column);
        argInfo.setLineno(expr.info.lineno);
        argInfo.setAddress(expr.info.address);
        args.info = argInfo;
        argList.add(args);
        return argList;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    Object expr____expr_ADD_expr(Object s1, Object s2, Object s3) throws Exception {
        // 1. check if expr1.value_type matches with the expr2.value_type
        // 2. etc.
        // 3. create and return node that has value_type
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          oper  = (Token         )s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;

        ParseTreeInfo.ExprInfo exprInfo = new ParseTreeInfo.ExprInfo();

        if(expr1.info.typename.equals("num") && expr2.info.typename.equals("num")) {
            // Okay
        }
        else {
            throw new Exception("[Error at " + expr1.info.lineno + ":" + (oper.column) + "] Binary operation " + oper.lexeme + " cannot be used with " + expr1.info.typename + " and " + expr2.info.typename + " values.");
        }
        //exprInfo.setExpr(expr);
        exprInfo.setTypename("num");
        exprInfo.setVal(expr1.info.val + oper.lexeme + expr2.info.val);
        exprInfo.setColumn(expr1.info.column);
        exprInfo.setLineno(expr1.info.lineno);
//        Double expr1Val = Double.parseDouble(expr1.info.val);
//        Double expr2Val = Double.parseDouble(expr2.info.val);
//        exprInfo.setValDouble(expr1Val-expr2Val);
        ParseTree.ExprAdd exprAdd = new ParseTree.ExprAdd(expr1, expr2);
        exprAdd.info = exprInfo;

        return exprAdd;
    }
    Object expr____expr_SUB_expr(Object s1, Object s2, Object s3) throws Exception {
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token op =  (Token)s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;

        ParseTreeInfo.ExprInfo exprInfo = new ParseTreeInfo.ExprInfo();

        if(expr1.info.typename.equals("num") && expr2.info.typename.equals("num")) {
            // Okay
        }
        else {
            throw new Exception("[Error at " + expr1.info.lineno + ":" + (op.column) + "] Binary operation " + op.lexeme + " cannot be used with " + expr1.info.typename + " and " + expr2.info.typename + " values.");
        }
        //exprInfo.setExpr(expr1);
        exprInfo.setTypename("num");
        exprInfo.setVal(expr1.info.val + op.lexeme + expr2.info.val);
        exprInfo.setColumn(expr1.info.column);
        exprInfo.setLineno(expr1.info.lineno);
//        Double expr1Val = Double.parseDouble(expr1.info.val);
//        Double expr2Val = Double.parseDouble(expr2.info.val);
//        exprInfo.setValDouble(expr1Val-expr2Val);
        ParseTree.ExprSub exprSub = new ParseTree.ExprSub(expr1,expr2);
        exprSub.info = exprInfo;

        return exprSub;
    }
    Object expr____expr_MUL_expr(Object s1, Object s2, Object s3) throws Exception {
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token op =  (Token)s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;

        if(expr1.info.typename.equals("num") && expr2.info.typename.equals("num")) {
            // Okay
        }
        else {
            throw new Exception("[Error at " + expr1.info.lineno + ":" + (op.column) + "] Binary operation " + op.lexeme + " cannot be used with " + expr1.info.typename + " and " + expr2.info.typename + " values.");
        }

        ParseTreeInfo.ExprInfo exprInfo = new ParseTreeInfo.ExprInfo();
        exprInfo.setTypename("num");
        exprInfo.setVal(expr1.info.val + op.lexeme + expr2.info.val);
        exprInfo.setColumn(expr1.info.column);
        exprInfo.setLineno(expr1.info.lineno);
//        Double expr1Val = Double.parseDouble(expr1.info.val);
//        Double expr2Val = Double.parseDouble(expr2.info.val);
//        exprInfo.setValDouble(expr1Val-expr2Val);
        ParseTree.ExprMul exprMul = new ParseTree.ExprMul(expr1,expr2);
        exprMul.info = exprInfo;

        return exprMul;
    }
    Object expr____expr_DIV_expr(Object s1, Object s2, Object s3) throws Exception {
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token op =  (Token)s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;

        if(expr1.info.typename.equals("num") && expr2.info.typename.equals("num")) {
            // Okay
        }
        else {
            throw new Exception("[Error at " + expr1.info.lineno + ":" + (op.column) + "] Binary operation " + op.lexeme + " cannot be used with " + expr1.info.typename + " and " + expr2.info.typename + " values.");
        }
        ParseTreeInfo.ExprInfo exprInfo = new ParseTreeInfo.ExprInfo();
        exprInfo.setTypename("num");
        exprInfo.setVal(expr1.info.val + op.lexeme + expr2.info.val);
        exprInfo.setColumn(expr1.info.column);
        exprInfo.setLineno(expr1.info.lineno);
//        Double expr1Val = Double.parseDouble(expr1.info.val);
//        Double expr2Val = Double.parseDouble(expr2.info.val);
//        exprInfo.setValDouble(expr1Val-expr2Val);
        ParseTree.ExprDiv exprDiv = new ParseTree.ExprDiv(expr1,expr2);
        exprDiv.info = exprInfo;

        return exprDiv;
    }
    Object expr____expr_MOD_expr(Object s1, Object s2, Object s3) throws Exception {
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token op =  (Token)s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;

        if(expr1.info.typename.equals("num") && expr2.info.typename.equals("num")) {
            // Okay
        }
        else {
            throw new Exception("[Error at " + expr1.info.lineno + ":" + (op.column) + "] Binary operation " + op.lexeme + " cannot be used with " + expr1.info.typename + " and " + expr2.info.typename + " values.");
        }
        ParseTreeInfo.ExprInfo exprInfo = new ParseTreeInfo.ExprInfo();
        exprInfo.setTypename("num");
        exprInfo.setVal(expr1.info.val + op.lexeme + expr2.info.val);
        exprInfo.setColumn(expr1.info.column);
        exprInfo.setLineno(expr1.info.lineno);
        ParseTree.ExprMod exprMod = new ParseTree.ExprMod(expr1,expr2);
        exprMod.info = exprInfo;

        return exprMod;
    }
    Object expr____expr_EQ_expr(Object s1, Object s2, Object s3) throws Exception {
        // 1. check if expr1.value_type matches with the expr2.value_type
        // 2. etc.
        // 3. create and return node that has value_type
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          oper  = (Token         )s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;

        if(expr1.info.typename.equals(expr2.info.typename) && !expr1.info.typename.equals("bool")) {
            // Okay
        }
        else {
            throw new Exception("[Error at " + expr1.info.lineno + ":" + (oper.column) + "] Binary operation " + oper.lexeme + " cannot be used with " + expr1.info.typename + " and " + expr2.info.typename + " values.");
        }
        ParseTreeInfo.ExprInfo exprInfo = new ParseTreeInfo.ExprInfo();
        exprInfo.setTypename("bool");
        exprInfo.setVal(expr1.info.val + oper.lexeme + expr2.info.val);
        exprInfo.setLineno(expr1.info.lineno);
        exprInfo.setColumn(expr1.info.column);
        ParseTree.ExprEq exprEq = new ParseTree.ExprEq(expr1,expr2);
        exprEq.info = exprInfo;
        return exprEq;
    }
    Object expr____expr_NE_expr(Object s1, Object s2, Object s3) throws Exception {
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          op =  (Token)s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;

        if(expr1.info.typename.equals(expr2.info.typename) && !expr1.info.typename.equals("bool")) {
            // Okay
        }
        else {
            throw new Exception("[Error at " + expr1.info.lineno + ":" + (op.column) + "] Binary operation " + op.lexeme + " cannot be used with " + expr1.info.typename + " and " + expr2.info.typename + " values.");
        }
        ParseTreeInfo.ExprInfo exprInfo = new ParseTreeInfo.ExprInfo();
        exprInfo.setTypename("bool");
        exprInfo.setVal(expr1.info.val + op.lexeme + expr2.info.val);
        exprInfo.setColumn(expr1.info.column);
        exprInfo.setLineno(expr1.info.lineno);
        ParseTree.ExprNe exprNe = new ParseTree.ExprNe(expr1,expr2);
        exprNe.info = exprInfo;

        return exprNe;
    }
    Object expr____expr_LE_expr(Object s1, Object s2, Object s3) throws Exception {
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          op =  (Token)s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;


        if(expr1.info.typename.equals(expr2.info.typename) && !expr1.info.typename.equals("bool")) {
            // Okay
        }
        else {
            throw new Exception("[Error at " + expr1.info.lineno + ":" + (op.column) + "] Binary operation " + op.lexeme + " cannot be used with " + expr1.info.typename + " and " + expr2.info.typename + " values.");
        }
        ParseTreeInfo.ExprInfo exprInfo = new ParseTreeInfo.ExprInfo();
        exprInfo.setTypename("bool");
        exprInfo.setVal(expr1.info.val + op.lexeme + expr2.info.val);
        exprInfo.setColumn(expr1.info.column);
        exprInfo.setLineno(expr1.info.lineno);
        ParseTree.ExprLe exprLe = new ParseTree.ExprLe(expr1,expr2);
        exprLe.info = exprInfo;

        return exprLe;
    }
    Object expr____expr_LT_expr(Object s1, Object s2, Object s3) throws Exception {
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          op =  (Token)s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;

        if(expr1.info.typename.equals(expr2.info.typename) && !expr1.info.typename.equals("bool")) {
            // Okay
        }
        else {
            throw new Exception("[Error at " + expr1.info.lineno + ":" + (op.column) + "] Binary operation " + op.lexeme + " cannot be used with " + expr1.info.typename + " and " + expr2.info.typename + " values.");
        }
        ParseTreeInfo.ExprInfo exprInfo = new ParseTreeInfo.ExprInfo();
        exprInfo.setTypename("bool");
        exprInfo.setVal(expr1.info.val + op.lexeme + expr2.info.val);
        exprInfo.setColumn(expr1.info.column);
        exprInfo.setLineno(expr1.info.lineno);
        ParseTree.ExprLt exprLt = new ParseTree.ExprLt(expr1,expr2);
        exprLt.info = exprInfo;

        return exprLt;
    }
    Object expr____expr_GT_expr(Object s1, Object s2, Object s3) throws Exception {
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          op =  (Token)s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;

        if(expr1.info.typename.equals(expr2.info.typename) && !expr1.info.typename.equals("bool")) {
            // Okay
        }
        else {
            throw new Exception("[Error at " + expr1.info.lineno + ":" + (op.column) + "] Binary operation " + op.lexeme + " cannot be used with " + expr1.info.typename + " and " + expr2.info.typename + " values.");
        }
        ParseTreeInfo.ExprInfo exprInfo = new ParseTreeInfo.ExprInfo();
        exprInfo.setTypename("bool");
        exprInfo.setVal(expr1.info.val + op.lexeme + expr2.info.val);
        exprInfo.setColumn(expr1.info.column);
        exprInfo.setLineno(expr1.info.lineno);
        ParseTree.ExprGt exprGt = new ParseTree.ExprGt(expr1,expr2);
        exprGt.info = exprInfo;
        return exprGt;
    }
    Object expr____expr_GE_expr(Object s1, Object s2, Object s3) throws Exception {
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          op =  (Token)s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;

        if(expr1.info.typename.equals(expr2.info.typename) && !expr1.info.typename.equals("bool")) {
            // Okay
        }
        else {
            throw new Exception("[Error at " + expr1.info.lineno + ":" + (op.column) + "] Binary operation " + op.lexeme + " cannot be used with " + expr1.info.typename + " and " + expr2.info.typename + " values.");
        }
        ParseTreeInfo.ExprInfo exprInfo = new ParseTreeInfo.ExprInfo();
        exprInfo.setTypename("bool");
        exprInfo.setVal(expr1.info.val + op.lexeme + expr2.info.val);
        exprInfo.setColumn(expr1.info.column);
        exprInfo.setLineno(expr1.info.lineno);
        ParseTree.ExprGe exprGe = new ParseTree.ExprGe(expr1,expr2);
        exprGe.info = exprInfo;
        return exprGe;
    }
    Object expr____expr_AND_expr(Object s1, Object s2, Object s3) throws Exception {
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          op =  (Token)s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;

        if(expr1.info.typename.equals(expr2.info.typename) && !expr1.info.typename.equals("num")) {
            // Okay
        }
        else {
            throw new Exception("[Error at " + expr1.info.lineno + ":" + (op.column) + "] Binary operation " + op.lexeme + " cannot be used with " + expr1.info.typename + " and " + expr2.info.typename + " values.");
        }
        ParseTreeInfo.ExprInfo exprInfo = new ParseTreeInfo.ExprInfo();
        exprInfo.setTypename("bool");
        exprInfo.setVal(expr1.info.val + op.lexeme + expr2.info.val);
        exprInfo.setColumn(expr1.info.column);
        exprInfo.setLineno(expr1.info.lineno);
        ParseTree.ExprAnd exprAnd = new ParseTree.ExprAnd(expr1,expr2);
        exprAnd.info = exprInfo;
        return exprAnd;
    }
    Object expr____expr_OR_expr(Object s1, Object s2, Object s3) throws Exception {
        ParseTree.Expr expr1 = (ParseTree.Expr)s1;
        Token          op =  (Token)s2;
        ParseTree.Expr expr2 = (ParseTree.Expr)s3;

        if(expr1.info.typename.equals(expr2.info.typename) && !expr1.info.typename.equals("num")) {
            // Okay
        }
        else {
            throw new Exception("[Error at " + expr1.info.lineno + ":" + (op.column) + "] Binary operation " + op.lexeme + " cannot be used with " + expr1.info.typename + " and " + expr2.info.typename + " values.");
        }
        ParseTreeInfo.ExprInfo exprInfo = new ParseTreeInfo.ExprInfo();
        exprInfo.setTypename("bool");
        exprInfo.setVal(expr1.info.val + op.lexeme + expr2.info.val);
        exprInfo.setColumn(expr1.info.column);
        exprInfo.setLineno(expr1.info.lineno);
        ParseTree.ExprOr exprOr = new ParseTree.ExprOr(expr1,expr2);
        exprOr.info = exprInfo;
        return exprOr;
    }
    Object expr____NOT_expr (Object s1, Object s2) throws Exception {
        Token          op =  (Token)s1;
        ParseTree.Expr expr = (ParseTree.Expr)s2;

        if(expr.info.typename.equals("num")) {
            throw new Exception("[Error at " + expr.info.lineno + ":" + (op.column) + "] Unary operation " + op.lexeme + " cannot be used with " + expr.info.typename + " value.");
        }
        ParseTreeInfo.ExprInfo exprInfo = new ParseTreeInfo.ExprInfo();
        exprInfo.setTypename("bool");
        exprInfo.setVal(op.lexeme + expr.info.val);
        exprInfo.setLineno(op.lineno);
        exprInfo.setColumn(op.column);
        ParseTree.ExprNot notExpr = new ParseTree.ExprNot(expr);
        notExpr.info = exprInfo;
        return notExpr;
    }
    Object expr____LPAREN_expr_RPAREN(Object s1, Object s2, Object s3) throws Exception {
        // 1. create and return node whose value_type is the same to the expr.value_type
        Token          lparen = (Token         )s1;
        ParseTree.Expr expr   = (ParseTree.Expr)s2;
        Token          rparen = (Token         )s3;

        ParseTreeInfo.ExprInfo exprInfo = new ParseTreeInfo.ExprInfo();
        exprInfo.setTypename(expr.info.typename);
        exprInfo.setVal(lparen.lexeme + expr.info.val + rparen.lexeme);
        exprInfo.setColumn(lparen.column);
        exprInfo.setLineno(lparen.lineno);

        ParseTree.ExprParen exprParen = new ParseTree.ExprParen(expr);
        exprParen.info = exprInfo;
        return exprParen;
    }
    Object expr____IDENT(Object s1) throws Exception {
        // 1. check if id.lexeme can be found in chained symbol tables
        // 2. check if it is variable type
        // 3. etc.
        // 4. create and return node that has the value_type of the id.lexeme
        Token id = (Token)s1;
        ParseTreeInfo.ExprInfo exprInfo = new ParseTreeInfo.ExprInfo();

        if(env.Get(id.lexeme) != null) {
            // okay
            if(env.Get(id.lexeme) instanceof ParseTree.TypeSpec) {
                ParseTree.TypeSpec id_type = (ParseTree.TypeSpec)env.Get(id.lexeme);
                exprInfo.setTypename(id_type.typename);
                exprInfo.setVal(id.lexeme);
                exprInfo.setColumn(id.column);
                exprInfo.setLineno(id.lineno);
                exprInfo.setAddress(id_type.info.address);
            }
            else {
                throw new Exception("[Error at " + id.lineno + ":" + id.column + "] Identifier " + id.lexeme + " should be a variable.");
            }
        }
        else {
            throw new Exception("[Error at " + id.lineno + ":" + (id.column) + "] Identifier " + id.lexeme + " is not defined.");
        }

        ParseTree.ExprIdent expr = new ParseTree.ExprIdent(id.lexeme);

        expr.info = exprInfo;

        expr.reladdr = expr.info.address;

        return expr;
    }
    Object expr____IDENT_LPAREN_args_RPAREN(Object s1, Object s2, Object s3, Object s4) throws Exception {
        // 1. check if id.lexeme can be found in chained symbol tables
        // 2. check if it is function type
        // 3. check if the number and types of env(id.lexeme).params match with those of args
        // 4. etc.
        // 5. create and return node that has the value_type of env(id.lexeme).return_type
        Token                    id   = (Token                   )s1;
        Token                    lparen = (Token                 )s2;
        ArrayList<ParseTree.Arg> args = (ArrayList<ParseTree.Arg>)s3;
        Token                    rparen = (Token                 )s4;

        if(env.Get(id.lexeme) == null) {
            throw new Exception("[Error at " + id.lineno + ":" + id.column + "] Identifier " + id.lexeme + " is not defined.");
        }
        if(env.Get(id.lexeme) instanceof ParseTree.TypeSpec) {
            throw new Exception("[Error at " + id.lineno + ":" + id.column + "] Identifier " + id.lexeme + " should be a function.");
        }

        ArrayList<Object> funcInfo = (ArrayList<Object>) env.Get(id.lexeme);
        ArrayList<Object> paramInfo = (ArrayList<Object>)funcInfo.get(2);
        if(paramInfo.size() != args.size()) {
            throw new Exception("[Error at " + id.lineno + ":" + id.column + "] Function " + id.lexeme + lparen.lexeme + rparen.lexeme + " should be called with the correct number of arguments.");
        }
        for(int i = 0; i < args.size(); i++) {
            ParseTree.Arg arg = args.get(i);
            ParseTree.Param param = (ParseTree.Param) paramInfo.get(i);
            if(!arg.info.typename.equals(param.info.typename)) {
                throw new Exception("[Error at " + arg.info.lineno + ":" + arg.info.column + "] The " + (i + 1) + "st argument of function " + id.lexeme + lparen.lexeme + rparen.lexeme + " should be " + param.info.typename + " value, instead of " + arg.info.typename + " value.");
            }
        }
        ParseTree.ExprFuncCall exprFuncCall = new ParseTree.ExprFuncCall(id.lexeme, args);
        ParseTreeInfo.ExprInfo exprInfo = new ParseTreeInfo.ExprInfo();
        exprInfo.setTypename((String)funcInfo.get(1));
        exprInfo.setVal(id.lexeme + lparen.lexeme + args.toString() + rparen.lexeme);
        exprInfo.setId(id.lexeme);
        exprInfo.setColumn(id.column);
        exprInfo.setLineno(id.lineno);
        exprInfo.setAddress(localDeclRelAddrCounter);
        exprFuncCall.info = exprInfo;

        return exprFuncCall;
    }
    Object expr____NUMLIT(Object s1) {
        // 1. create and return node that has int type
        Token token = (Token)s1;

        ParseTreeInfo.ExprInfo exprInfo = new ParseTreeInfo.ExprInfo();
        exprInfo.setTypename("num");
        exprInfo.setVal(token.lexeme);
        exprInfo.setColumn(token.column);
        exprInfo.setLineno(token.lineno);
        double value = Double.parseDouble(token.lexeme);
        ParseTree.ExprNumLit expr = new ParseTree.ExprNumLit(value);
        exprInfo.setExpr(expr);
        expr.info = exprInfo;
        return expr;
    }
    Object expr____BOOLLIT(Object s1) {
        Token token = (Token)s1;

        ParseTreeInfo.ExprInfo exprInfo = new ParseTreeInfo.ExprInfo();
        exprInfo.setTypename("bool");
        exprInfo.setVal(token.lexeme);
        exprInfo.setColumn(token.column);
        exprInfo.setLineno(token.lineno);

        boolean value = Boolean.parseBoolean(token.lexeme);
        ParseTree.ExprBoolLit expr = new ParseTree.ExprBoolLit(value);
        exprInfo.setExpr(expr);
        expr.info = exprInfo;
        return expr;
    }
    Object expr____NEW_primtype_LBRACKET_expr_RBRACKET (Object s1, Object s2, Object s3, Object s4, Object s5) throws Exception {
        Token newSymbol = (Token)s1;
        ParseTree.TypeSpec primType = (ParseTree.TypeSpec)s2;
        Token lbracket = (Token)s3;
        ParseTree.Expr expr = (ParseTree.Expr)s4;
        Token rbracket = (Token)s5;
        ParseTreeInfo.ExprInfo exprInfo = new ParseTreeInfo.ExprInfo();
        exprInfo.setTypename(primType.typename);
        exprInfo.setVal(newSymbol.lexeme + primType.typename + lbracket.lexeme + expr.info.val + rbracket.lexeme);
        exprInfo.setColumn(newSymbol.column);
        exprInfo.setLineno(newSymbol.lineno);
        ParseTree.ExprArrayNew exprArrayNew = new ParseTree.ExprArrayNew(primType, expr);
        exprArrayNew.info = exprInfo;

        return exprArrayNew;
    }
    Object expr____IDENT_LBRACKET_expr_RBRACKET (Object s1, Object s2, Object s3, Object s4) throws Exception {
        Token id = (Token)s1;
        Token lbracket = (Token)s2;
        ParseTree.Expr expr = (ParseTree.Expr)s3;
        Token rbracket = (Token)s4;

        ParseTree.TypeSpec id_type = (ParseTree.TypeSpec) env.Get(id.lexeme);
        if(id_type == null) {
            throw new Exception("[Error at " + id.lineno + ":" + id.column + "] Identifier " + id.lexeme + " is not defined.");
        }
        if(!id_type.typename.equals("bool[]") && !id_type.typename.equals("num[]")) {
            throw new Exception("[Error at " + id.lineno + ":" + id.column + "] Identifier " + id.lexeme + " should be an array variable.");
        }
        else if(!expr.info.typename.equals("num")) {
            throw new Exception("[Error at " + expr.info.lineno + ":" + expr.info.column + "] Array index must be num value.");
        }
        ParseTreeInfo.ExprInfo exprInfo = new ParseTreeInfo.ExprInfo();
        exprInfo.setTypename(id_type.typename.replace("[]", ""));
        exprInfo.setVal(id.lexeme + lbracket.lexeme + expr.info.val + rbracket.lexeme);
        exprInfo.setColumn(id.column);
        exprInfo.setLineno(id.lineno);
        exprInfo.setAddress(id_type.info.address);
        ParseTree.ExprArrayElem exprArrayElem = new ParseTree.ExprArrayElem(id.lexeme, expr);
        exprArrayElem.info = exprInfo;
        exprArrayElem.reladdr = id_type.info.address;
        return exprArrayElem;
    }
    Object expr____IDENT_DOT_SIZE (Object s1, Object s2, Object s3) throws Exception {
        Token id = (Token)s1;
        Token dot = (Token)s2;
        Token sizeKeyword = (Token)s3;
        if(env.Get(id.lexeme) != null) {
            // okay
            if(env.Get(id.lexeme) instanceof ParseTree.TypeSpec) {
                ParseTree.TypeSpec id_type = (ParseTree.TypeSpec)env.Get(id.lexeme);
                if(!id_type.typename.equals("bool[]") && !id_type.typename.equals("num[]")) {
                    throw new Exception("[Error at " + id.lineno + ":" + id.column + "] Identifier " + id.lexeme + " should be an array variable.");
                }
            }
            else {
                throw new Exception("[Error at " + id.lineno + ":" + id.column + "] Identifier " + id.lexeme + " should be a variable.");
            }
        }
        else {
            throw new Exception("[Error at " + id.lineno + ":" + (id.column) + "] Identifier " + id.lexeme + " is not defined.");
        }
        ParseTree.ExprArraySize exprArraySize = new ParseTree.ExprArraySize(id.lexeme);
        ParseTreeInfo.ExprInfo exprInfo = new ParseTreeInfo.ExprInfo();
        exprInfo.setTypename("num");
        exprInfo.setVal(id.lexeme + dot.lexeme + sizeKeyword.lexeme);
        exprInfo.setColumn(id.column);
        exprInfo.setLineno(id.lineno);
        ParseTree.TypeSpec id_type = (ParseTree.TypeSpec)env.Get(id.lexeme);
        exprInfo.setAddress(id_type.info.address);
        exprArraySize.info = exprInfo;
        exprArraySize.reladdr = id_type.info.address;
        return exprArraySize;
    }
}
