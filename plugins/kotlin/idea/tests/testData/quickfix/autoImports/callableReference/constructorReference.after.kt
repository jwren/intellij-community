// "Import class 'Some'" "true"
// ERROR: Unresolved reference: Some
package p1

import p2.Some
import p2.receiveSomeCtor

fun a() {
    receiveSomeCtor(::Some<selection><caret></selection>)
}
