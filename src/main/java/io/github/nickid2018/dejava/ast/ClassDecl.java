package io.github.nickid2018.dejava.ast;

import io.github.nickid2018.dejava.ConstantNames;
import io.github.nickid2018.dejava.api.IModifier;

import java.util.ArrayList;
import java.util.List;

public class ClassDecl implements INode, IModifiable {

    private final ClassType classType;
    private final List<INode> children = new ArrayList<>();
    protected ModifierList modifiers = new ModifierList();
    private String identifier;
    private List<TypeArgumentDecl> typeArguments;
    private List<Typename> classImplements = new ArrayList<>();
    private List<Typename> classPermits = new ArrayList<>();
    private String classExtends;

    public ClassDecl(String identifier, ClassType t) {
        this.identifier = identifier;
        this.classType = t;
    }

    public ClassDecl(String identifier) {
        this(identifier, ClassType.CLASS);
    }

    public List<TypeArgumentDecl> getTypeArguments() {
        return typeArguments;
    }

    public ClassDecl setTypeArguments(List<TypeArgumentDecl> typeArguments) {
        this.typeArguments = typeArguments;
        return this;
    }

    public ClassType getClassType() {
        return classType;
    }

    @Override
    public List<IModifier> getModifiers() {
        return modifiers.getModifiers();
    }

    @Override
    public ClassDecl addModifiers(IModifier... modifier) {
        modifiers.addModifiers(modifier);
        return this;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public List<Typename> getImplements() {
        return classImplements;
    }

    public ClassDecl setImplements(List<Typename> classImplements) {
        this.classImplements = classImplements;
        return this;
    }

    public List<Typename> getPermits() {
        return classPermits;
    }

    public ClassDecl setPermits(List<Typename> classPermits) {
        this.classPermits = classPermits;
        return this;
    }

    public String getExtends() {
        return classExtends;
    }

    public ClassDecl setExtends(String classExtends) {
        this.classExtends = classExtends;
        return this;
    }

    @Override
    public String toSource(FormatControl fc) {

        return new StructuralWriter(fc)
                .append(getModifiersString())
                .token(getClassType().keyword, getIdentifier())
                .doIf(typeArguments, (s, w) -> w.append(TypeArgumentDecl.listToSource(fc, s)))
                .doIf(classExtends, (s, w) -> w.token(ConstantNames.EXTENDS, s))
                .doIf(classImplements, (s, w) -> w.token(ConstantNames.IMPLEMENTS).tokenSep(", ", s.stream().map(e -> e.toSource(fc)).toArray(String[]::new)))
                .doIf(classPermits, (s, w) -> w.token(ConstantNames.PERMITS).tokenSep(", ", s.stream().map(e -> e.toSource(fc)).toArray(String[]::new)))
                .block(b -> {
                    children.forEach(e -> b.line(e.toSource()));
                })
                .toSource();
    }

    public enum ClassType {
        CLASS(ConstantNames.CLASS),
        INTERFACE(ConstantNames.INTERFACE),
        ENUM(ConstantNames.ENUM),
        ANNOTATION(ConstantNames.ANNOTATION),
        RECORD(ConstantNames.RECORD);

        public final String keyword;

        ClassType(String t) {
            this.keyword = t;
        }
    }

}
